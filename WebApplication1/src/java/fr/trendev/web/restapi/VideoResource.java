/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web.restapi;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

@Path("videos")
public class VideoResource {
    
    @Context
    UriInfo info;
    
    @Resource(name = "java:comp/DefaultManagedExecutorService")
    ManagedExecutorService executor;
    
    private static final Logger logger =
            Logger.getLogger(VideoResource.class.getCanonicalName());
    
    private final static java.nio.file.Path SRC_FOLDER = Paths.get(System.
            getProperty(
                    "user.home"), "Movies");
    
    private final static String[] exts = {"m4v", "mp4", "m4p"};
    
    @GET
    @Path("{name}.{ext}")
    @Produces("video/mp4")
    public Response getVideoStream(@PathParam("name") String name,
            @PathParam("ext") String ext,
            @Context Request request) {

        //checks if the extension is known and supported for this method
        if (!Arrays.asList(exts).contains(ext)) {
            logger.log(Level.WARNING,
                    ".{0} is not a supported video type", ext);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        //opens a path to the required file
        String filename = name.concat(".").concat(ext);
        java.nio.file.Path file = SRC_FOLDER.resolve(filename);
        
        if (!Files.exists(file)) {
            logger.log(Level.WARNING, "File \"{0}\" cannot be found!", filename);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        CacheControl cc = new CacheControl();
        cc.setPrivate(true);
        cc.setMaxAge(0);
        cc.setMustRevalidate(true);
        cc.setNoCache(true);
        cc.setNoStore(true);
        
        logger.log(Level.INFO,
                "{0} has changed or has never been cached",
                filename);

        //creates a response with the inputstream and the picture type
        /*builder = Response.ok(Files.
                        newInputStream(file), "video/mp4");*/
        //Creates a streaming output and copy the file to it
        StreamingOutput stream = (OutputStream output) -> {
            logger.log(Level.INFO,
                    "Copying file {0} in the streaming output...",
                    filename);
            
            try {
                long size = Files.copy(file, output);
                logger.log(Level.INFO,
                        "Copying {0} Bytes of file {1} in the streaming output : [OK]",
                        new Object[]{size, filename});
            } catch (IOException ex) {
                logger.log(Level.SEVERE,
                        "SEVERE ERROR copying file {0} in the stream : {1}",
                        new Object[]{filename, ex.getMessage()});
            }
        };
        
        Response.ResponseBuilder builder = Response.ok(stream, "video/mp4").
                cacheControl(cc);
        
        return builder.build();
        
    }
}
