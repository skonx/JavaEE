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
import javax.faces.component.UIOutput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
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

    private static final long serialVersionUID = 1L;

    public static final long bound = 500;

    private final String WebMessages_BASENAME = "WebMessages";
    private final String WebMessages_BUNDLE_CONGRAT = "Congrat";

    /**
     * Avoid to use VV or zzzz or XXX in the pattern if time / timezone is not
     * kept.
     */
    //private final String pattern = "EEEE dd MMMM uuuu HH:mm:ss - VV - zzzz - XXX";
    private final String pattern = "EEEE dd MMMM uuuu";

    /**
     * This field is used to render the hidden value "text" in the facelet. Text
     * is rendered if the condition (iter > bound) is true. Another solution
     * should be to render this item using the condition and the attribute
     * render in the facelet.
     */
    private UIOutput text = null;

    /**
     * This field is used to render a hidden value "fulldate" in the facelet.
     * The output will be the specified date using the cust. local parameter.
     */
    private UIOutput fulldate = null;

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
    }

    public long getBound() {
        return bound;
    }

    public UIOutput getText() {
        return text;
    }

    public void setText(UIOutput text) {
        this.text = text;
    }

    public UIOutput getFulldate() {
        return fulldate;
    }

    public void setFulldate(UIOutput fulldate) {
        this.fulldate = fulldate;
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

    /**
     * Called by the link action. Increments iter, reset incr to 0 and redirect
     * the user to "index.html". Logs the back link call.
     *
     * @throws IOException
     */
    public void back() throws IOException {
        iter++;
        //incr = 0;
        ExternalContext ec = FacesContext.getCurrentInstance().
                getExternalContext();
        logger.log(Level.INFO, "BACK LINK ");
        ec.
                redirect(ec.getRequestContextPath());
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

        if (text != null) {
            ResourceBundle bundle = ResourceBundle.getBundle(
                    WebMessages_BASENAME, this.getFacesContextLocale());
            String value = bundle.getString(WebMessages_BUNDLE_CONGRAT) + " "
                    + bound;
            text.setValue((iter > bound) ? value : "");
        }

        logger.log(Level.INFO, "And now, iter = {0}", iter);
    }

    public void reset() {
        logger.log(Level.WARNING, "~~ Reset called ~~");
        setIncr(0);
        setDate(Date.from(Instant.now()));
        fulldate.setValue(this.convertDate(date));

        /*date = null;
        getFulldate().setValue("");*/
    }

    public void showFullDateMessage(AjaxBehaviorEvent event) {
        logger.log(Level.WARNING, "showFullDateMessage() called !!!");
        fulldate.setValue(convertDate(date));
        fulldate.setRendered(true);
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
