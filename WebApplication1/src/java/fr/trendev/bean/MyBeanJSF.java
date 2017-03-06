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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jsie
 */
//@ManagedBean(eager = true)
@Named
@SessionScoped
public class MyBeanJSF implements Serializable {

    public static final long bound = 500;

    private final String WebMessages_BASENAME = "WebMessages";
    private final String WebMessages_BUNDLE_CONGRAT = "Congrat";

    /**
     * Avoid to use VV or zzzz or XXX in the pattern if time / timezone is not
     * kept.
     */
    //private final String pattern = "EEEE dd MMMM uuuu HH:mm:ss - VV - zzzz - XXX";
    private final String pattern = "EEEE dd MMMM uuuu";

    private String text;

    private String textarea;

    //@ManagedProperty(value = "#{param.sn}")
    /**
     * Session's Name
     */
    private String sn;

    private long iter;

    @NotNull
    @Min(0)
    @MyMax(bound)
    private long incr;

    @NotNull
    private Date date;

    public static final Logger logger =
            Logger.getLogger(MyBeanJSF.class.getCanonicalName());

    /**
     * Creates a new instance of MyBeanJSF
     */
    public MyBeanJSF() {
    }

    public Date getDate() {
        return date;
    }

    /**
     * Translates a java.util.Date into java.time.LocalDate and logs the date.
     *
     * @param date the date specified in the form
     */
    public void setDate(Date date) {
        this.date = date;

        logger.log(Level.INFO, "Date field is : {0}", this.
                convertDate(date));
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
        logger.log(Level.INFO, "Incremental value is now {0}", incr);

    }

    public long getBound() {
        return bound;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextarea() {
        return textarea;
    }

    public void setTextarea(String textarea) {
        logger.log(Level.INFO, textarea);
        this.textarea = textarea;
    }

    /**
     * Post init. Gets the http session id from the FacesContext. Logs the call.
     */
    @PostConstruct
    public void init() {
        iter = 1;
        incr = 0;
        sn = null;

        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null) {

            HttpSession session = (HttpSession) fc.getExternalContext().
                    getSession(true);

            sn = fc.getExternalContext().getSessionId(true);

            if (session instanceof HttpSession) {
                logger.log(Level.INFO, "Session is a HttpSession : {0}",
                        session.getId());

                int value = new Double(Math.floor(Math.random() * bound) + 1).
                        intValue();
                session.setAttribute(MyBeanJSF.class.getSimpleName(), value);

                HttpServletRequest req = (HttpServletRequest) fc.
                        getExternalContext().getRequest();

                //stores the remote address and the remote host into the session
                session.setAttribute("Remote_Address", req.getRemoteAddr());
                session.setAttribute("Remote_Host", req.getRemoteHost());
            }

        }

        logger.log(Level.WARNING, "MyBeanJSF.init()  : NEW INIT {0}",
                new String[]{
                    LocalDateTime.now().toString()});
    }

    /**
     * Called by the link action. Increments iter and redirect the user to
     * "index.html". Logs the back link call.
     *
     * @return index
     * @throws IOException
     */
    public String back() throws IOException {
        iter++;

        logger.log(Level.INFO, "BACK LINK ");

        /*ExternalContext ec = FacesContext.getCurrentInstance().
                getExternalContext();
        ec.redirect(ec.getRequestContextPath());*/
        return "index.html?faces-redirect=true";

    }

    /**
     * Action performed when the "incr" field is changed (unfocused). Logs iter
     * and iter value before increment and then logs the iter result. Renders
     * and set the value of the hidden field "text" if not null.
     *
     * @param event the event send from AJAX
     * @throws AbortProcessingException
     */
    public void add(AjaxBehaviorEvent event)
            throws AbortProcessingException {
        logger.log(Level.INFO, "iter = {0}", iter);
        logger.log(Level.INFO, "incr = {0}", incr);
        iter += incr;

        ResourceBundle bundle = ResourceBundle.getBundle(
                WebMessages_BASENAME, this.getFacesContextLocale());
        String value = bundle.getString(WebMessages_BUNDLE_CONGRAT) + " "
                + bound;
        text = ((iter > bound) ? value : "");

        logger.log(Level.INFO, "And now, iter = {0}", iter);
    }

    public void reset() {
        logger.log(Level.WARNING, "~~ Reset called ~~");
        setIncr(0);
        setDate(Date.from(Instant.now()));
    }

    private String convertDate(Date date) {

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
                        ofPattern(pattern, this.getFacesContextLocale()));

        return d;
    }

    private Locale getFacesContextLocale() {
        return FacesContext.getCurrentInstance().
                getViewRoot().getLocale();
    }
}
