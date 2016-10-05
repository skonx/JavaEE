/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.bean;

import fr.trendev.constraints.MyMax;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jsie
 */
//@ManagedBean(eager = true)
@Named
@SessionScoped
public class MyBeanJSF implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final long bound = 100;

    //@ManagedProperty(value = "#{param.sn}")
    /**
     * Session Name
     */
    private String sn;

    private long iter;

    //TODO : override this annotation with a custom message in french
    @MyMax(bound)
    @NotNull
    private long incr;

    public static final Logger logger
            = Logger.getLogger(MyBeanJSF.class.getCanonicalName());

    /**
     * Creates a new instance of MyBeanJSF
     */
    public MyBeanJSF() {
    }

    public String getSn() {

        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public long getIter() {
        return iter;
    }

    public void setIter(long iter) {
        this.iter = iter;
    }

    public long getIncr() {
        return incr;
    }

    public void setIncr(long incr) {
        this.incr = incr;
    }

    public long getBound() {
        return bound;
    }

    @PostConstruct
    public void init() {
        iter = 1;
        incr = 0;
        sn = null;

        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null) {
            sn = fc.getExternalContext().getSessionId(true);
        }

        logger.log(Level.WARNING, "NEW INIT {0}: {1}", new String[]{sn, LocalDateTime.now().toString()});
    }

    public void back() throws IOException {
        iter++;
        incr = 0;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        logger.log(Level.INFO, "BACK LINK ");
        ec.redirect(ec.getRequestContextPath() + "/index.html");
    }

    public void add() {
        iter += incr;
    }

}
//
