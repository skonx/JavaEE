/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service : Load a picture from the Documents folder of the user
 *
 * @author jsie
 */
@Path("images")
public class ImageResource {

    @Context
    UriInfo info;

    @Resource(name = "java:comp/DefaultManagedExecutorService")
    ManagedExecutorService executor;

    /**
     * Default logger
     */
    private static final Logger logger =
            Logger.getLogger(ImageResource.class.getCanonicalName());

    private final static String VAULT_NAME = "vault";

    /**
     * The path to the source folder containing the pictures
     */
    private final static java.nio.file.Path SRC_FOLDER = Paths.get(System.
            getProperty(
                    "user.home"), /*"Documents",*/ VAULT_NAME);

    private final static java.nio.file.Path TMP_FOLDER = Paths.get(System.
            getProperty(
                    "user.home"), "Deposit_tmp");

    //private final static java.nio.file.Path TRG_FOLDER = 
    /**
     * A set of extension supported and controlled by this REST Web Service
     */
    private final static String[] exts = {"jpeg", "jpg", "png"};

    /**
     * Provides a source stream to a specified picture.
     *
     * @param filename the picture to display.
     * @param ext the extension of the picture.
     * @param request the HTTP request
     * @return A HTTP response with an InputStream and the picture type.
     */
    @GET
    @Path("{name}.{ext}")
    @Produces({"image/jpeg", "image/png"})
    public Response getPicture(@PathParam("name") String filename,
            @PathParam("ext") String ext,
            @Context Request request) {

        //checks if the extension is known and supported for this method
        if (!Arrays.asList(exts).contains(ext)) {
            logger.log(Level.WARNING,
                    ".{0} is not a supported extension for a picture", ext);
            return Response.status(Status.NOT_FOUND).build();
        }

        //opens a path to the required file
        filename = filename.concat(".").concat(ext);
        java.nio.file.Path file = SRC_FOLDER.resolve(filename);

        if (!Files.exists(file)) {
            logger.log(Level.WARNING, "File \"{0}\" cannot be found!", filename);
            return Response.status(Status.NOT_FOUND).build();
        }

        try {
            CacheControl cc = new CacheControl();
            cc.setPrivate(true);

            Instant instant = Files.getLastModifiedTime(file).toInstant();
            Date lastModifiedTime = Date.from(instant);

            Response.ResponseBuilder builder = request.evaluatePreconditions(
                    lastModifiedTime);

            if (builder == null) {
                logger.log(Level.INFO,
                        "{0} has changed or has never been cached",
                        filename);
                //creates a response with the inputstream and the picture type
                builder = Response.ok(Files.
                        newInputStream(file), "image/" + ext);
                builder.lastModified(lastModifiedTime);
            }

            builder.cacheControl(cc);

            return builder.build();

        } catch (IOException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.
                    getMessage()).build();
        }
    }

    /**
     * Provides a list with the names of the stored pictures
     *
     * @return if successful, returns a JSON object including the list with the
     * names of the pictures
     *
     */
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPicturesList() {

        logger.log(Level.INFO, "Providing the pictures list");

        if (!Files.exists(SRC_FOLDER)) {
            logger.log(Level.WARNING, "The source folder cannot be found!!!");
            return Response.status(Status.NOT_FOUND).build();
        }

        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        JsonArrayBuilder list = factory.createArrayBuilder();//Json.createArrayBuilder();

        DirectoryStream.Filter<java.nio.file.Path> filter = e -> (e.
                getFileName().toString().endsWith(".jpg")
                || e.getFileName().
                        toString().endsWith(".png"));

        try {
            Files.newDirectoryStream(SRC_FOLDER, filter).forEach(e -> list.
                    add(e.getFileName().toString()));
        } catch (IOException ex) {
            logger.log(Level.SEVERE,
                    "Error occured while listing the pictures folder {0}: !!!",
                    ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.
                    getMessage()).build();
        }

        JsonObjectBuilder value = factory.createObjectBuilder()
                .add("list", list);

        return Response.ok(value.build()).build();
    }

    /**
     * Stores the provided picture in a tmp folder and then moves it to a vault
     *
     * @param ar the asynchronous response
     * @param ct the Content-Type of the HTTP POST Request, must be "image/jpeg"
     * or "image/png"
     * @param length the document's size
     * @param is an input stream provided by the document
     */
    @POST
    @Asynchronous
    @Consumes({"image/jpeg", "image/png"})
    public void savePicture(@Suspended final AsyncResponse ar, @HeaderParam(
            "Content-Type") String ct,
            @HeaderParam("Content-Length") long length,
            InputStream is,
            @DefaultValue("false") @QueryParam("small") boolean small) {

        final String path = info.getPath();

        logger.log(Level.INFO, "REST Web Service {0}: POST request",
                ImageResource.class.getCanonicalName());

        executor.submit(() -> {
            logger.log(Level.INFO,
                    "REST Web Service {0}: Executor submit() started",
                    ImageResource.class.getName());
            Response response;

            try {
                logger.log(Level.INFO, "Request's headers :");
                logger.log(Level.INFO, "Content-Type : {0}", ct);
                logger.log(Level.INFO, "Content-Length : {0}", length);

                logger.log(Level.INFO, "Generate a filename");
                String filename = generateRandomName(ct);
                logger.log(Level.INFO, "File will be stored in : {0}", filename);

                if (!Files.exists(TMP_FOLDER)) {
                    Files.createDirectory(TMP_FOLDER);
                }

                /*saves the stream (request content) into a temporary file*/
                java.nio.file.Path file = TMP_FOLDER.resolve(filename);

                /*Reduces the picture*/
                if (small) {
                    logger.log(Level.INFO, "Process reduce image size...");

                    if (!Files.exists(file)) {
                        Files.createFile(file);
                    }
                    BufferedImage inputImage = ImageIO.read(is);
                    int width = 150;
                    int height = 150;
                    // creates output image
                    /*If the inputImage type is unknow, will fix it to TYPE_INT_ARGB*/
                    BufferedImage outputImage = new BufferedImage(width,
                            height, inputImage.getType()
                            == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_ARGB : inputImage.
                                            getType());

                    // scales the input image to the output image
                    Graphics2D g2d = outputImage.createGraphics();
                    g2d.drawImage(inputImage, 0, 0, width, height, null);
                    g2d.dispose();

                    ImageIO.write(outputImage, ct.split("/")[1],
                            file.toFile());

                    logger.log(Level.INFO, "Size of {1} = {0} Bytes",
                            new String[]{
                                String.valueOf(Files.size(file)), file.
                                getFileName().
                                toString()});
                    logger.log(Level.INFO, "Reduce Image : OK");
                } else {
                    /*Stores the original picture*/
                    logger.log(Level.INFO, "Copy the stream in : {0}", filename);
                    long size = Files.copy(is, file,
                            StandardCopyOption.REPLACE_EXISTING);
                    logger.log(Level.INFO, "Copy : OK");

                    logger.log(Level.INFO, "Size of {0} = {1} Bytes",
                            new Object[]{
                                filename,
                                size});

                }

                if (!Files.exists(SRC_FOLDER)) {
                    Files.createDirectory(SRC_FOLDER);
                }
                logger.log(Level.INFO, "Move {0} into {1}", new String[]{
                    filename, VAULT_NAME});
                /*moves the file to its vault*/
                Files.move(file, SRC_FOLDER.resolve(file.getFileName()),
                        StandardCopyOption.REPLACE_EXISTING);
                logger.log(Level.INFO, "Move : OK");

                logger.log(Level.INFO, "Prepare response...");

                response = Response.status(Status.CREATED).entity(filename).
                        location(URI.
                                create(path + "/"
                                        + VAULT_NAME + "/"
                                        + filename)).build();

                logger.log(Level.INFO, "Response : OK");

            } catch (IOException | IllegalArgumentException ex1) {
                try {
                    is.close();
                } catch (IOException ex2) {
                    logger.log(Level.SEVERE,
                            "Error closing the inputstream in the REST Web Service {0}:\n{1}",
                            new Object[]{ImageResource.class.getCanonicalName(),
                                ex2.getMessage()});
                } finally {
                    logger.
                            log(Level.SEVERE,
                                    "Error during picture saving : {0}",
                                    ex1.getMessage());

                    response = Response.serverError().entity(ex1.getMessage()).
                            build();
                }
            }

            logger.log(Level.INFO,
                    "Resume the asynchronous request and send the response...");

            ar.resume(response);
        });

        logger.log(Level.INFO, "REST Web Service {0}: end of POST request",
                ImageResource.class.getCanonicalName());
    }

    /**
     * Reads the bytes of an input stream
     *
     * @param is an input stream to pump
     * @return the length of the input stream
     * @throws IOException if an error occurs while reading in the input stream
     */
    private static long pumpStream(InputStream is) throws IOException {
        byte[] buffer = new byte[2048];

        int count = 0;
        int b;

        while ((b = is.read(buffer)) != -1) {
            count += b;
        }

        return count;
    }

    /**
     * Generates a random name for the file, based on UUID.randomUUID(). Only
     * supports jpg/png extensions.
     *
     * @param ct the Content-Type of the HTTP POST request
     * @return
     */
    private static String generateRandomName(String ct) throws
            IllegalArgumentException {

        if (ct == null) {
            throw new IllegalArgumentException(
                    "Content-Type can not be null or blank");
        }

        return UUID.randomUUID().toString() + "."
                + (("image/jpeg").equals(ct) ? "jpg" : "png");
    }
}
