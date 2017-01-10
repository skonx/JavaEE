/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
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

    //TODO : JAX-RS getPicturesList
    @GET
    @Path("list")
    @Produces("text/html")
    public Response getPicturesList() {
        if (!Files.exists(SRC_FOLDER)) {
            logger.log(Level.WARNING, "The source folder cannot be found!!!");
            return Response.status(Status.NOT_FOUND).build();
        }

        StringBuilder sb = new StringBuilder();

        try {
            sb.append(Files.list(SRC_FOLDER).count());
        } catch (IOException ex) {
            Logger.getLogger(ImageResource.class.getName()).
                    log(Level.SEVERE,
                            "Error occured during picture list export : {0}",
                            ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.
                    getMessage()).build();

        }

        return Response.status(Status.FOUND).entity(sb.toString()).build();
    }

    /**
     * Stores the provided picture in a tmp folder and then moves it to a vault
     *
     * @param ct the Content-Type of the HTTP POST Request, must be "image/jpeg"
     * or "image/png"
     * @param length the document's size
     * @param is an inputstream provided by the document
     * @return if successful, a response with the name of the new created file
     */
    @POST
    @Consumes({"image/jpeg", "image/png"})
    public Response savePicture(@HeaderParam("Content-Type") String ct,
            @HeaderParam("Content-Length") long length,
            InputStream is) {
        logger.log(Level.INFO, "REST Web Service {0}: POST request",
                ImageResource.class.getCanonicalName());

        String filename = generateRandomName(ct);

        try {
            //should be not the expected value
            logger.log(Level.INFO, "Content-Type : {0}", ct);
            logger.log(Level.INFO, "Content-Length : {0}", length);

            if (!Files.exists(TMP_FOLDER)) {
                Files.createDirectory(TMP_FOLDER);
            }

            /*saves the stream (request content) into a temporary file*/
            java.nio.file.Path file = TMP_FOLDER.resolve(filename);

            long size = Files.copy(is, file,
                    StandardCopyOption.REPLACE_EXISTING);

            logger.log(Level.INFO, "Size of {0} = {1} Bytes", new Object[]{
                filename,
                size});

            if (!Files.exists(SRC_FOLDER)) {
                Files.createDirectory(SRC_FOLDER);
            }

            /*moves the file to its vault*/
            Files.move(file, SRC_FOLDER.resolve(file.getFileName()),
                    StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ex1) {
            try {
                is.close();
            } catch (IOException ex2) {
                logger.log(Level.SEVERE,
                        "Error closing the inputstream in the REST Web Service {0}:\n{1}",
                        new Object[]{ImageResource.class.getCanonicalName(),
                            ex2.getMessage()});
            } finally {
                Response.serverError().entity(ex1.getMessage()).build();
            }
        }

        return Response.status(Status.CREATED).entity(filename).location(URI.
                create(info.getPath() + "/"
                        + VAULT_NAME + "/"
                        + filename)).build();
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
    private static String generateRandomName(String ct) {

        return UUID.randomUUID().toString() + "."
                + (ct.equals("image/jpeg") ? "jpg" : "png");
    }
}
