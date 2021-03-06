/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web.restapi;

import fr.trendev.bean.ActiveSessionTracker;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

@Path("videos")
public class VideoResource {

    @Context
    UriInfo info;

    @EJB
    ActiveSessionTracker tracker;

    private static final Logger logger =
            Logger.getLogger(VideoResource.class.getCanonicalName());

    private final static java.nio.file.Path SRC_FOLDER = Paths.get(System.
            getProperty(
                    "user.home"), "Movies");

    private final static int BUFFER_SIZE = 65536;//2048;

    private static long getProgress(final HttpSession session) {
        return (long) session.getAttribute("PROGRESS");
    }

    private static void setProgress(final HttpSession session, long progress) {
        session.setAttribute("PROGRESS", progress);
    }

    @GET
    @Produces({"video/x-m4v", "video/mp4"})
    public Response getVideoStream(@HeaderParam(
            "Range") String range,
            @Context HttpServletRequest request/*,
            @Context HttpServletResponse response*/) throws IOException {

        String filename = "IMG_6346.m4v";

        //Programmatic security check
        /*try {
            if (request.authenticate(response)) {
                logger.log(Level.INFO, "AUTHENTICATION : [ OK ] {0} {1} {2}",
                        new Object[]{request.isUserInRole("TutorialUser"),
                            request.getRemoteUser(), request.getUserPrincipal()});
            } else {
                logger.log(Level.INFO, "AUTHENTICATION : [ FAILED ]");
            }
        } catch (ServletException ex) {
            logger.log(Level.WARNING, "Error : Invalid AUTHENTICATION");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }*/
        HttpSession session = request.getSession(false);

        if (!tracker.contains(session)) {
            logger.log(Level.WARNING,
                    "Error : UNAUTHORIZED access to file {0}, INVALID SESSION ID {1}",
                    new Object[]{filename, Objects.nonNull(session) ? session.
                        getId() : "[NULL]"});
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else {
            logger.log(Level.INFO, "AUTHORIZED access to file {0}",
                    filename);
        }

        java.nio.file.Path file = SRC_FOLDER.resolve(filename);

        logger.log(Level.INFO, "Requested Range: {0}", range);

        if (range == null || range.isEmpty()) {
            return Response.status(
                    Response.Status.REQUESTED_RANGE_NOT_SATISFIABLE).build();
        }

        if (!Files.exists(file)) {
            logger.
                    log(Level.WARNING, "File \"{0}\" cannot be found !",
                            filename);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            String rg = range.split("=")[1];

            long start = Integer.parseInt(rg.split("-")[0]);

            long size = file.toFile().length();
            /* end will be the file size if no bound is provided*/
            long end = (rg.split("-").length == 2) ? Long.parseLong(rg.split(
                    "-")[1]) : size - 1;

            if (end < start || end > size - 1) {
                logger.log(Level.WARNING,
                        "{0} is not a valid range for file {1}, should be from 0 to {2}",
                        new Object[]{
                            range,
                            filename,
                            size - 1});
                return Response.status(
                        Response.Status.REQUESTED_RANGE_NOT_SATISFIABLE).build();
            }

            logger.log(Level.INFO, "Sending range {0}-{1} of file {2}",
                    new Object[]{
                        start, end, filename});
            /*configure the streaming output before to put it inside the response*/
            StreamingOutput stream = (OutputStream output) -> {
                logger.log(Level.INFO,
                        "Opening streaming output and providing range {0}-{1} of file {2} ...",
                        new Object[]{
                            start, end, filename});

                try (SeekableByteChannel channel = Files.newByteChannel(file,
                        StandardOpenOption.READ);) {

                    channel.position(start);

                    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

                    int read = 0;
                    int count;
                    int unread = new Long(end + 1).intValue();
                    int b;

                    buffer.clear();

                    while (((b = channel.read(buffer)) != -1)
                            && (read < end + 1)) {
                        count = (unread < b) ? unread : b;
                        read += count;
                        output.write(buffer.array(), 0,
                                count);
                        unread -= b;

                        if ((getProgress(session) < start + read)
                                && (start <= getProgress(session))) {
                            setProgress(session, start + read);
                        }

                        logger.log(Level.FINE,
                                "File {0} - STREAMING : {1} / {2} Bytes | PROGRESS = {3}",
                                new Object[]{
                                    filename, start + read, end + 1,
                                    getProgress(session)});
                        buffer.clear();
                    }

                    logger.log(Level.INFO,
                            "Providing Range {0}-{1} of file {2} : [OK]",
                            new Object[]{
                                start, end, filename});
                    if ((start + read) == size && getProgress(session) == size) {
                        logger.log(Level.INFO,
                                "Providing file {0} : [ COMPLETE ]",
                                new Object[]{filename});
                        setProgress(session, 0);
                    }

                } catch (IOException ex) {
                    logger.log(Level.WARNING,
                            "Copying file {0} in the stream : {1}",
                            new Object[]{filename, ex.getMessage() != null ? ex.
                                getMessage() : ex});
                }

            };

            Response.ResponseBuilder builder = Response.status(
                    Response.Status.PARTIAL_CONTENT).
                    entity(stream).
                    header("Accept-Ranges", "bytes").
                    header("Content-Range", String.format("bytes %s-%s/%s",
                            start,
                            end, size)).
                    header("Content-Length", end + 1 - start)/*.
                    header("Content-Type", "video/x-m4v")*/;

            return builder.build();
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING,
                    "A bound in {0} is not a valid number", range);
            return Response.status(
                    Response.Status.REQUESTED_RANGE_NOT_SATISFIABLE).build();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error providing {0} : {1}",
                    new Object[]{
                        filename, e});
            return Response.serverError().build();
        }
    }
}
