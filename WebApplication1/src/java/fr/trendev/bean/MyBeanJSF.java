/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.bean;

import fr.trendev.constraints.MyMax;
import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
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

    //private final String pattern = "EEEE dd MMMM uuuu HH:mm:ss - VV - zzzz - XXX";
    private final String pattern = "EEEE dd MMMM uuuu";

    //@ManagedProperty(value = "#{param.sn}")
    /**
     * Session's Name
     */
    private String sn;

    private long iter;

    @MyMax(bound)
    @NotNull
    private long incr;

    private Date date;

    public static final Logger logger
            = Logger.getLogger(MyBeanJSF.class.getCanonicalName());

    /**
     * Creates a new instance of MyBeanJSF
     */
    public MyBeanJSF() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        Instant instant = date.toInstant();
        //ZonedDateTime can also be used and the pattern cab be extend using additionnal zone information
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        //LocalDate is used for Date only
        LocalDate ld = zdt.toLocalDate();
        //LocalDateTime is used for Date and Time only
        //LocalDateTime ld = zdt.toLocalDateTime();
        //For present date and time
        //LocalDateTime ld = LocalDateTime.now();
        String d = ld.
                format(DateTimeFormatter.
                        ofPattern(pattern, Locale.FRENCH));
        System.out.println("Date field is : " + d);
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
            HttpSession session = (HttpSession) fc.getExternalContext().
                    getSession(true);
            if (session instanceof HttpSession) {
                logger.log(Level.INFO, "Session is a HttpSession : {0}",
                        session.getId());
            }

        }

        logger.log(Level.WARNING, "NEW INIT {0}: {1}", new String[]{sn,
            LocalDateTime.now().toString()});
    }

    public void back() throws IOException {
        iter++;
        incr = 0;
        ExternalContext ec = FacesContext.getCurrentInstance().
                getExternalContext();
        logger.log(Level.INFO, "BACK LINK ");
        ec.redirect(ec.getRequestContextPath() + "/index.html");
    }

    public void add(AjaxBehaviorEvent event)
            throws AbortProcessingException {
        logger.log(Level.INFO, "iter = {0}", iter);
        logger.log(Level.INFO, "incr = {0}", incr);
        iter += incr;
        logger.log(Level.INFO, "And now, iter = {0}", iter);
    }

}
//
