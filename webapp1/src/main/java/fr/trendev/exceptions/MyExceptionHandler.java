/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.exceptions;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;

/**
 *
 * @author jsie
 */
public class MyExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;

    public static final Logger logger
            = Logger.getLogger(MyExceptionHandler.class.getCanonicalName());

    public MyExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void handle() throws FacesException {
        ExternalContext ec = FacesContext.getCurrentInstance().
                getExternalContext();
        for (ExceptionQueuedEvent event : getUnhandledExceptionQueuedEvents()) {
            Throwable e = event.getContext().getException();
            logger.log(Level.SEVERE, "ExceptionHandler : {0}", e.getMessage());
            if (e instanceof ViewExpiredException) {
                try {
                    logger.log(Level.SEVERE,
                            "Automatic redirection to the welcome page");
                    ec.redirect(ec.getRequestContextPath());
                } catch (IOException ex) {
                    Logger.getLogger(MyExceptionHandler.class.getName()).
                            log(Level.SEVERE, ex.getMessage());
                }
            }
            getWrapped().handle();
        }
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

}
