package com.mycompany.itineraryplanner2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jiayu on 06/11/2015.
 */
public class Routes implements JsonDeserializer<RouteInfo> {
    static HashMap<RouteInfoKey, RouteInfo> routes;

    void generateRoutes() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RouteInfo.class, new Routes());


    }

    @Override
    public RouteInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String transportMode;
        String origin;
        String destination;
        int time;
        int cost;
        for (Map.Entry<String, JsonElement> transportModeEntry :
                json.getAsJsonObject().entrySet()) {
            transportMode = transportModeEntry.getValue().getAsString();
            JsonObject originJson = transportModeEntry.getValue().getAsJsonObject();
            for (Map.Entry<String, JsonElement> originEntry :
                    originJson.getAsJsonObject().entrySet()) {
                origin = originEntry.getValue().getAsString();
                JsonObject destinationJson = originEntry.getValue().getAsJsonObject();
                    destination = destinationJson.getAsString();
                    time = destinationJson.getAsJsonObject().getAsJsonPrimitive("time").getAsInt();
                    cost = destinationJson.getAsJsonObject().getAsJsonPrimitive("cost").getAsInt();
                    return new RouteInfo(TransportMode.getTransportMode(transportMode), time, cost);
            }

        }
    }
}
}
