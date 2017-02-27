/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.bean;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BiConsumer;
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

    private final Map<String, HttpSession> ast;

    public ActiveSessionTracker() {
        ast = Collections.synchronizedMap(new TreeMap<>());
    }

    public boolean add(HttpSession session) {
        if (Objects.isNull(session)) {
            return false;
        }
        return ast.put(session.getId(), session) == session;
    }

    public boolean remove(HttpSession session) {
        if (Objects.isNull(session)) {
            return false;
        }
        return ast.remove(session.getId()) == session;
    }

    public boolean contains(HttpSession session) {
        if (Objects.isNull(session)) {
            return false;
        }
        return ast.containsKey(session.getId());
    }

    public boolean isEmpty() {
        return ast.isEmpty();
    }

    public void forEach(BiConsumer<? super String, ? super HttpSession> c) {
        ast.forEach(c);
    }
}
