/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web;

import fr.trendev.bean.MyBeanJSF;
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

    @Inject
    private MyBeanJSF myBeanJSF;

    @GET
    @Produces("text/plain")
    public String getValues() {
        String result = "\nBOUND  = " + myBeanJSF.getBound();
        result += "\nITER = " + myBeanJSF.getIter();
        result += "\nINCR  = " + myBeanJSF.getIncr();
        return result;
    }

}
