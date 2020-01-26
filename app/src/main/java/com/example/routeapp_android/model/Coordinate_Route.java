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

public class Coordinate_Route implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Coordinate_RouteId id;
    
    private Route route;
    
    private Coordinate coordinate;
    
    private Integer wayOrder;
    private Long visited;

    /**
     * @return the route
     */
    public Route getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     * @return the coordinate
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * @param coordinate the coordinate to set
     */
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * @return the order
     */
    public Integer getOrder() {
        return wayOrder;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(Integer order) {
        this.wayOrder = order;
    }

    /**
     * @return the visited
     */
    public Long getVisited() {
        return visited;
    }

    /**
     * @param visited the visited to set
     */
    public void setVisited(Long visited) {
        this.visited = visited;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.coordinate);
        hash = 37 * hash + Objects.hashCode(this.wayOrder);
        hash = 37 * hash + Objects.hashCode(this.visited);
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
        final Coordinate_Route other = (Coordinate_Route) obj;
        if (!Objects.equals(this.route, other.route)) {
            return false;
        }
        if (!Objects.equals(this.coordinate, other.coordinate)) {
            return false;
        }
        if (!Objects.equals(this.wayOrder, other.wayOrder)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Coordinate_Route{" + "route=" + route + ", order=" + wayOrder + '}';
    }

    
}
