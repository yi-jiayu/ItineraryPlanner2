package com.mycompany.itineraryplanner2;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Jiayu on 06/11/2015.
 */
public class Routes {
    static HashMap<RouteInfoKey, RouteInfo> routes = new HashMap<>();

    static void generateRoutes(InputStream jsonInputStream) throws IOException {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(jsonInputStream, "UTF-8"));
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String tm = jsonReader.nextName();
            TransportMode transportMode = TransportMode.getTransportMode(tm);
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String origin = jsonReader.nextName();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String destination = jsonReader.nextName();
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        jsonReader.skipValue();
                        int cost = jsonReader.nextInt();
                        jsonReader.skipValue();
                        int time = jsonReader.nextInt();
                        Routes.routes.put(new RouteInfoKey(transportMode, origin, destination), new RouteInfo(time, cost));
                    }
                    jsonReader.endObject();
                }
                jsonReader.endObject();
            }
            jsonReader.endObject();
        }
        jsonReader.endObject();
    }
}
