/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.routeapp_android.model;

import java.util.Set;

/**
 *
 * @author Jon Calvo Gaminde
 */

public class FullRoute {
    private Route route;
    private Set<Direction> directions;

    public FullRoute() {
    }

    public FullRoute(Route route, Set<Direction> directions) {
        this.route = route;
        this.directions = directions;
    }

    
    
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
     * @return the directions
     */
    public Set<Direction> getDirections() {
        return directions;
    }

    /**
     * @param directions the directions to set
     */
    public void setDirections(Set<Direction> directions) {
        this.directions = directions;
    }
    
}
