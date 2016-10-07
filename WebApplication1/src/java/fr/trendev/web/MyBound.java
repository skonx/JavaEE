/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web;

import fr.trendev.bean.MyBeanJSF;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
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
@Singleton
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
            logger.log(Level.INFO, "SESSION ID = {0}", myBeanJSF.getSn());
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
    public String testSessionID() {

        boolean result;
        
        if (Objects.nonNull(myBeanJSF.getSn()) && Objects.nonNull(req.getSession(true)) && Objects.nonNull(req.getSession(true).getId())) {
            result = myBeanJSF.getSn().equals(req.getSession(true).getId());
        } else {
            result = false;
        }

        String message = "TEST = " + result;
        message += ("\nmyBeanJSF.getSn() = " + myBeanJSF.getSn());
        message += ("\nreq.getSession(true).getId() = " + req.getSession(true).getId());
        return message;
    }
}
