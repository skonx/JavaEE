/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web.filter;

import fr.trendev.bean.ActiveSessionTracker;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jsie
 */
/*servletNames = {"Faces Servlet", "javax.ws.rs.core.Application",
    "MyServletTest"}*/
@WebFilter(urlPatterns = {"/*"}, asyncSupported = true)
public class MyFilter implements Filter {

    @EJB
    ActiveSessionTracker tracker;

    public static final Logger LOG =
            Logger.getLogger(MyFilter.class.getCanonicalName());

    public MyFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        LOG.log(Level.INFO, "## A request has been filtered ##");
        LOG.log(Level.INFO, "Active sessions =>");
        tracker.forEach((id, s) -> LOG.
                log(Level.INFO, "id={0}, session={1}", new Object[]{id, s}));
        LOG.log(Level.INFO, "<= Active sessions");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        //catch an existing session bound with the request (or not)
        HttpSession session = req.getSession(false);
        //catch the the session id of the request
        String sid = req.
                getRequestedSessionId();

        LOG.log(Level.INFO, "Requested Session id = {0}", sid);

        if (session == null) {
            LOG.log(Level.WARNING,
                    "Filter : request not bound with a session");
        } else {
            LOG.log(Level.INFO, "Session id of the request = {0}", session.
                    getId());
            LOG.log(Level.INFO, "Check if {0} is an active session...",
                    session.getId());
            LOG.log(Level.INFO, "{0} is active = {1}", new Object[]{session.
                getId(),
                tracker.contains(session)});

            if (req.getUserPrincipal() != null) {
                LOG.log(Level.INFO,
                        "FILTER ==> UserPrincipal = [{0}] ; AuthType={1} ; RemoteUser = [{2}]",
                        new Object[]{req.
                                    getUserPrincipal().getName(), req.
                                    getAuthType(),
                            req.getRemoteUser()});
            } else {
                LOG.log(Level.WARNING, "### No Authentication ###");
            }

            //adds a re-created session (preserve session across redeployment) in the tracker
            if (!tracker.contains(session)) {
                LOG.log(Level.WARNING,
                        "Session id {0} should be active... adding it in the tracker",
                        sid);
                tracker.add(session);
            }
        }

        LOG.log(Level.INFO, "Requested URL = {0} ; requested session id = {1}",
                new Object[]{req.getRequestURL().
                            toString(), sid});

        //Allow the client's browser to cache the JSF resources (JavaScript, CSS...) 
        /*if (req.getRequestURI().startsWith(req.getContextPath()
                + ResourceHandler.RESOURCE_IDENTIFIER)) {
            resp.setHeader("Cache-Control",
                    "private, max-age=31536000"); // HTTP 1.1. 
            //resp.setDateHeader("Expires", 0); // Proxies.
        }*/
        /**
         * Cookie JSESSIONID : Reset the expiration date
         */
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            Arrays.asList(cookies).stream()
                    .filter(c -> "JSESSIONID".equals(c.getName()) && session
                            != null)
                    .forEach(c -> {

                        c.setMaxAge(300);//5 minutes, should be equal to the server session duration
                        c.setValue(session.getId());
                        c.setPath(req.getContextPath());
                        resp.addCookie(c);

                        LOG.log(Level.INFO,
                                "Path : [{3}]; Cookie : [{0}] ; value : [{1}] ; expiration : [{2}] seconds",
                                new Object[]{c.getName(), c.getValue(), c.
                                    getMaxAge(), c.getPath()});
                    });
        }
        chain.doFilter(request, response);

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.log(Level.INFO, "Filter initialized");
    }

    @Override
    public void destroy() {
        LOG.log(Level.INFO, "Filter destroyed");
    }
}
