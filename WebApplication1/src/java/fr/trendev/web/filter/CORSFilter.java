/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web.filter;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author jsie
 */
@Provider
public class CORSFilter implements
        ContainerResponseFilter {

    private static final Logger logger = Logger.getLogger(CORSFilter.class.
            getName());

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext response) {

        logger.log(Level.INFO, "Adding CORS ...");

        response.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        response.getHeaders().
                putSingle("Access-Control-Allow-Methods",
                        "OPTIONS, GET, POST, PUT, DELETE");
        response.getHeaders().
                putSingle("Access-Control-Allow-Headers", "Content-Type");
    }

}
