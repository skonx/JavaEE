/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.bean;

import java.io.Serializable;

/**
 *
 * @author jsie
 */
public class MyBean implements Serializable {

    private int iter;
    private String svname;

    public int getIter() {
        return iter;
    }

    public void setIter(int iter) {
        this.iter = iter;
    }

    /**
     * Get the value of svname
     *
     * @return the value of svname
     */
    public String getSvname() {
        return svname;
    }

    /**
     * Set the value of svname
     *
     * @param svname new value of svname
     */
    public void setSvname(String svname) {
        this.svname = svname;
    }

}
