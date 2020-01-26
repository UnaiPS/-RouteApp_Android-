/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.routeapp_android.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Jon Calvo Gaminde
 */

public class Session implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private User logged;
    private String code;
    private Date lastAction;

    

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    

    /**
     * @return the logged
     */
    public User getLogged() {
        return logged;
    }

    /**
     * @param logged the logged to set
     */
    public void setLogged(User logged) {
        this.logged = logged;
    }

    /**
     * @return the lastAction
     */
    public Date getLastAction() {
        return lastAction;
    }

    /**
     * @param lastAction the lastAction to set
     */
    public void setLastAction(Date lastAction) {
        this.lastAction = lastAction;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.logged);
        hash = 23 * hash + Objects.hashCode(this.code);
        hash = 23 * hash + Objects.hashCode(this.lastAction);
        return hash;
    }

    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Session other = (Session) obj;
        if (!Objects.equals(this.logged, other.logged)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Session{" + "logged=" + logged + ", lastAction=" + lastAction + '}';
    }
    
}
