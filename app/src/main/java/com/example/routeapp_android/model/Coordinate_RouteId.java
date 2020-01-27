/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.routeapp_android.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author 2dam
 */
public class Coordinate_RouteId implements Serializable {
    private Long routeId;
    private Long coordinateId;

    /**
     * @return the routeId
     */
    public Long getRouteId() {
        return routeId;
    }

    /**
     * @param routeId the routeId to set
     */
    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    /**
     * @return the coordinateId
     */
    public Long getCoordinateId() {
        return coordinateId;
    }

    /**
     * @param coordinateId the coordinateId to set
     */
    public void setCoordinateId(Long coordinateId) {
        this.coordinateId = coordinateId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.routeId);
        hash = 29 * hash + Objects.hashCode(this.coordinateId);
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
        final Coordinate_RouteId other = (Coordinate_RouteId) obj;
        if (!Objects.equals(this.routeId, other.routeId)) {
            return false;
        }
        if (!Objects.equals(this.coordinateId, other.coordinateId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Coordinate_RouteId{" + "routeId=" + routeId + ", coordinateId=" + coordinateId + '}';
    }

    
}
