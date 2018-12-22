/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.web;

import fr.trendev.bean.ActiveSessionTracker;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author jsie
 */
public class NewServletListener implements ServletContextListener,
        HttpSessionListener, HttpSessionIdListener {

    private static final Logger logger = Logger.getLogger(
            NewServletListener.class.
                    getName());

    @EJB
    private ActiveSessionTracker tracker;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.log(Level.INFO, "contextInitialized: {0}", LocalDateTime.now().
                toString());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.log(Level.INFO, "contextDestroyed: {0}", LocalDateTime.now().
                toString());
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        long progress = 0l;
        logger.log(Level.INFO,
                "sessionCreated {0} and \"PROGRESS\" attribute for VIDEO STREAMING is {2}: {1}",
                new Object[]{se.
                            getSession().getId(), LocalDateTime.now().toString(),
                    progress});
        se.getSession().setAttribute("PROGRESS", progress);
        tracker.add(se.getSession());
    }

    /*
     * @param se 
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.log(Level.INFO, "sessionDestroyed {0}: {1}", new Object[]{se.
            getSession().getId(), LocalDateTime.now().toString()});
        HttpSession s = se.getSession();
        logger.log(Level.INFO, "Session id={0} has {1}been removed",
                new Object[]{
                    s.getId(), tracker.remove(s) ? "" : "not "});

        for (Enumeration<String> e = s.getAttributeNames(); e.hasMoreElements();) {
            String attribute = e.nextElement();
            System.out.println("Attribute [" + attribute + "] = " + s.
                    getAttribute(attribute));
        }
    }

    @Override
    public void sessionIdChanged(HttpSessionEvent se, String oldSessionId) {
        logger.log(Level.WARNING,
                "ID CHANGED : Session with id {0} has changed and is now {1}",
                new Object[]{oldSessionId, se.getSession().getId()});

        logger.log(Level.WARNING, "{0} has {1}been removed", new Object[]{
            oldSessionId, tracker.remove(oldSessionId) ? "" : "not "});

        logger.log(Level.WARNING,
                "{0} has {1}been added in the tracker instead of {2}",
                new Object[]{
                    se.getSession().getId(),
                    tracker.add(se.getSession()) ? "" : "not ", oldSessionId});

    }
}
