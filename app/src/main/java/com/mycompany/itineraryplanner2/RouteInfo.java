package com.mycompany.itineraryplanner2;

/**
 * Created by Jiayu on 06/11/2015.
 */
public class RouteInfo {
    TransportMode transportMode;
    int time;
    int cost;

    public RouteInfo(TransportMode transportMode, int time, int cost) {
        this.transportMode = transportMode;
        this.time = time;
        this.cost = cost;
    }
}
