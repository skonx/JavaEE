/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jsie
 */
@Startup
@Singleton
@DeclareRoles("WebApp1User")
public class ActiveSessionTracker {

    private final static int DEFAULT_SIZE = 10;
    private final List<HttpSession> list;

    public ActiveSessionTracker() {
        list = new ArrayList<>(DEFAULT_SIZE);
    }

    public boolean add(HttpSession session) {
        return list.add(session);
    }

    public boolean remove(HttpSession session) {
        return list.remove(session);
    }

    public boolean contains(HttpSession session) {
        return list.contains(session);
    }

    /**
     * Test if the active session tracker is empty or not. Requirement : a
     * principal must be authenticated and the associated session must be valid
     *
     * @return true if the tracker is empty, false otherwise
     */
    @RolesAllowed("WebApp1User")
    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void forEach(Consumer<? super HttpSession> c) {
        list.forEach(c);
    }
}
