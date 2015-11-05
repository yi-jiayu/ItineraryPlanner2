package com.mycompany.itineraryplanner2;

/**
 * Created by Jiayu on 06/11/2015.
 */
public enum TransportMode {
    TAXI, PUBLIC_TRANSPORT, FOOT;

    static TransportMode getTransportMode(String transportMode) {
        switch (transportMode) {
            case "taxi":
                return TAXI;
            case "pt":
                return PUBLIC_TRANSPORT;
            case "foot":
                return FOOT;
            default:
                return TAXI;
        }
    }
}
