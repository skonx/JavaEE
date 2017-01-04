/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * REST Web Service : Load a picture from the Documents folder of the user
 *
 * @author jsie
 */
@Path("images")
public class ImageResource {

    /**
     * Default logger
     */
    private static final Logger logger
            = Logger.getLogger(ImageResource.class.getCanonicalName());

    /**
     * The path to the source folder containing the pictures
     */
    private final static java.nio.file.Path SRC_FOLDER = Paths.get(System.
            getProperty(
                    "user.home"), "Documents");

    /**
     * A set of extension supported and controlled by this REST Web Service
     */
    private final static String[] exts = {"jpeg", "jpg", "png"};

    /**
     * Provide a source stream to a specified picture.
     *
     * @param filename the picture to display.
     * @param ext the extension of the picture.
     * @param request the HTTP request
     * @return A HTTP response with an InputStream and the picture type.
     * @throws IOException if something goes wrong reading the file.
     */
    @GET
    @Path("{name}.{ext}")
    @Produces({"image/jpeg", "image/png"})
    public Response getPicture(@PathParam("name") String filename,
            @PathParam("ext") String ext,
            @Context Request request) {

        //check if the extension is known and supported for this method
        if (!Arrays.asList(exts).contains(ext)) {
            logger.log(Level.WARNING,
                    ".{0} is not a supported extension for a picture", ext);
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        //open a path to the required file
        filename = filename.concat(".").concat(ext);
        java.nio.file.Path dest = SRC_FOLDER.resolve(filename);

        if (!Files.exists(dest)) {
            return Response.status(Status.NOT_FOUND).build();
        }

        try {
            CacheControl cc = new CacheControl();
            cc.setPrivate(true);

            Instant instant = Files.getLastModifiedTime(dest).toInstant();
            Date lastModifiedTime = Date.from(instant);

            Response.ResponseBuilder builder = request.evaluatePreconditions(
                    lastModifiedTime);

            if (builder == null) {
                logger.log(Level.INFO,
                        "{0} has changed or has never been cached",
                        filename);
                //create a response with the inputstream and the picture type
                builder = Response.ok(Files.
                        newInputStream(dest), "image/" + ext);
                builder.lastModified(lastModifiedTime);
            }

            builder.cacheControl(cc);

            return builder.build();

        } catch (IOException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.
                    getMessage()).build();
        }

    }

}
