/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web;

import fr.trendev.bean.MyBeanJSF;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

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

}
