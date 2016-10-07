/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.bean;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jsie
 */
@Startup
@Singleton
public class ActiveSessionTracker {

    private final static int DEFAULT_SIZE = 10;
    private final List<HttpSession> list;

    public ActiveSessionTracker() {
        list = new ArrayList<>(DEFAULT_SIZE);
    }

    public List<HttpSession> getList() {
        return list;
    }

    public boolean add(HttpSession session) {
        return list.add(session);
    }

    public boolean remove(HttpSession session) {
        return list.remove(session);
    }
}
