/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web;

import fr.trendev.bean.MyBeanJSF;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 * REST Web Service
 *
 */
@Path("/MB")
@Stateless
public class MyBound {

    public static final Logger logger
            = Logger.getLogger(MyBound.class.getCanonicalName());

    @Inject
    private MyBeanJSF myBeanJSF;
    @Context
    private HttpServletRequest req;

    @PostConstruct
    public void init() {
        if (myBeanJSF.getSn() == null) {
            myBeanJSF.setSn(req.getSession(true).getId());
            logger.info("SESSION ID = " + myBeanJSF.getSn());
        }
    }

    @GET
    @Path("bound")
    @Produces("text/plain")
    public long getBound() {
        return myBeanJSF.getBound();
    }

    @GET
    @Path("iter")
    @Produces("text/plain")
    public long getIter() {
        return myBeanJSF.getIter();
    }

    @GET
    @Path("incr")
    @Produces("text/plain")
    public long getIncr() {
        return myBeanJSF.getIncr();
    }

    @GET
    @Path("test")
    @Produces("text/plain")
    public boolean testSessionID() {
        return myBeanJSF.getSn().equals(req.getSession(true).getId());
    }
}
