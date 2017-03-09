/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.bean;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jsie
 */
@Named(value = "myBeanJSF2")
@RequestScoped
public class MyBeanJSF2 {

    public static final Logger logger =
            Logger.getLogger(MyBeanJSF2.class.getCanonicalName());

    /**
     * Creates a new instance of MyBeanJSF2
     */
    public MyBeanJSF2() {
    }

    public void logout() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(
                false);
        if (session != null) {
            try {
                logger.log(Level.INFO, "Invalidating {0} created : {1}",
                        new Object[]{session.getId(), session.getCreationTime()});
                session.invalidate();

                ExternalContext ec = FacesContext.getCurrentInstance().
                        getExternalContext();
                ec.redirect(ec.getRequestContextPath());
            } catch (IOException ex) {
                Logger.getLogger(MyBeanJSF.class.getName()).
                        log(Level.SEVERE,
                                "Unable to redirect to index.html from logout()",
                                ex);
            }
        }
    }
}
