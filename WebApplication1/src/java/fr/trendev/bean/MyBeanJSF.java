/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.bean;

import java.io.IOException;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author jsie
 */
//@ManagedBean(eager = true)
@Named
@SessionScoped
public class MyBeanJSF implements Serializable {

    private static final long serialVersionUID = 1L;

    //@ManagedProperty(value = "#{param.svname}")
    private String svname;

    private int iter;

    private static final Logger logger = Logger.getLogger("MyBeanJSF");

    /**
     * Creates a new instance of MyBeanJSF
     */
    public MyBeanJSF() {
    }

    public String getSvname() {
        return svname;
    }

    public void setSvname(String svname) {
        this.svname = svname;
    }

    public int getIter() {
        return iter;
    }

    public void setIter(int iter) {
        this.iter = iter;
    }

    @PostConstruct
    public void init() {
        iter = 1;
        svname = FacesContext.getCurrentInstance().getExternalContext().getSessionId(true);
        logger.log(Level.WARNING, "NEW INIT : {0}", LocalDateTime.now().toString());
    }

    public void back() throws IOException {
        iter++;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        logger.log(Level.INFO, "BACK LINK ");
        ec.redirect(ec.getRequestContextPath() + "/index.html");
    }
}
//
