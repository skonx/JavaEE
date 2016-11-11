/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web;

import fr.trendev.bean.ActiveSessionTracker;
import java.io.IOException;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jsie
 */
@WebFilter(servletNames = {"Faces Servlet"})
public class MyFilter implements Filter {

    @EJB
    ActiveSessionTracker tracker;

    public static final Logger logger
            = Logger.getLogger(MyFilter.class.getCanonicalName());

    public MyFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        logger.log(Level.INFO, "A request has been filtered");
        /*logger.log(Level.INFO, "Active sessions =>");
        tracker.getList().forEach(s -> logger.log(Level.INFO, s.getId()));
        logger.log(Level.INFO, "<= Active sessions");*/

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        logger.log(Level.INFO, "Requested Session id = {0}", req.
                getRequestedSessionId());

        if (session == null) {
            logger.log(Level.WARNING,
                    "Filter : request not bound with a session");
        } else {
            logger.log(Level.INFO, "Session id of the request = {0}", session.
                    getId());
            logger.log(Level.INFO, "Check if {0} is an active session...",
                    session.getId());
            logger.log(Level.INFO, "{0} is active = {1}", new Object[]{session.
                getId(),
                tracker.getList().
                contains(session)});
        }

        chain.doFilter(request, response);

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.log(Level.INFO, "Filter initialized");
    }

    @Override
    public void destroy() {
        logger.log(Level.INFO, "Filter destroyed");
    }
}
