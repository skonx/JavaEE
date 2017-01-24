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
import javax.ejb.Singleton;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author jsie
 */
@Singleton
public class NewServletListener implements ServletContextListener,
        HttpSessionListener {

    private static final Logger LOG = Logger.getLogger(NewServletListener.class.
            getName());

    @EJB
    private ActiveSessionTracker tracker;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.log(Level.INFO, "contextInitialized: {0}", LocalDateTime.now().
                toString());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.log(Level.INFO, "contextDestroyed: {0}", LocalDateTime.now().
                toString());
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        long progress = 0l;
        LOG.log(Level.INFO,
                "sessionCreated {0} and \"PROGRESS\" attribute for VIDEO STREAMING is {2}: {1}",
                new Object[]{se.
                            getSession().getId(), LocalDateTime.now().toString(),
                    progress});
        se.getSession().setAttribute("PROGRESS", progress);
        tracker.add(se.getSession());
    }

    /*
     * TODO: check why a destroyed session is restarted without a sessionCreated call
     * @param se 
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        LOG.log(Level.INFO, "sessionDestroyed {0}: {1}", new Object[]{se.
            getSession().getId(), LocalDateTime.now().toString()});
        HttpSession s = se.getSession();
        tracker.remove(s);

        for (Enumeration<String> e = s.getAttributeNames(); e.hasMoreElements();) {
            String attribute = e.nextElement();
            System.out.println("Attribute [" + attribute + "] = " + s.
                    getAttribute(attribute));
        }
    }
}
