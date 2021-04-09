package edu.itserulik.earthquakes.client.impl;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import edu.itserulik.earthquakes.model.Geometry;

import java.lang.reflect.Type;

public class GeometryDeserializer implements JsonDeserializer<Geometry> {

    @Override
    public Geometry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        var jsonObject = json.getAsJsonObject();
        var jsonCoordinates = jsonObject.get("coordinates").getAsJsonArray();
        return Geometry.builder()
                .longitude(jsonCoordinates.get(0).getAsDouble())
                .latitude(jsonCoordinates.get(1).getAsDouble())
                .build();
    }
}
