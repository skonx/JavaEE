/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web.restapi;

import fr.trendev.bean.ActiveSessionTracker;
import fr.trendev.web.filter.MyFilter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 */
@Path("/MB")
@Singleton
public class MyBound {

    public static final Logger LOG =
            Logger.getLogger(MyBound.class.getCanonicalName());

    //tracker is an EJB, should be injected using @EJB
    @EJB
    ActiveSessionTracker tracker;

    @PostConstruct
    public void init() {
        LOG.log(Level.INFO, "RESTAPI MyBound created");
    }

    /**
     * Provides the list of the active Http Session references in the custom
     * tracker. If no user is authenticated and no session are valid, will
     * return an UNAUTHORIZED HTTP message
     *
     * @param req
     * @return a JSON object including the list of the active sessions
     */
    @GET
    @Path("sessions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllActiveSessions(@Context HttpServletRequest req) {

        LOG.log(Level.INFO, "Providing the sessions list");

        if (tracker.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonArrayBuilder trackerList = factory.createArrayBuilder();

        tracker.forEach((id, session) -> {
            try {
                trackerList.add(
                        factory.createObjectBuilder().add("sessionId", id)
                                .add("creationTime", session.getCreationTime()));
            } catch (IllegalStateException e) {
                LOG.log(Level.WARNING,
                        "## session {0} invalidated but still in the tracker ## - REMOVED [{1}]",
                        new Object[]{id, tracker.remove(session) ? "YES" : "NO"});
            }
        });

        JsonObjectBuilder jsonlist = factory.createObjectBuilder()
                .add("trackerList", trackerList);

        MyFilter.LOG.log(Level.INFO,
                "MY BOUND REST-API ==> UserPrincipal = [{0}] ; AuthType={1} ; RemoteUser = [{2}]",
                new Object[]{req.
                            getUserPrincipal().getName(), req.getAuthType(),
                    req.getRemoteUser()});

        return Response.ok(jsonlist.build()).build();
    }
}
