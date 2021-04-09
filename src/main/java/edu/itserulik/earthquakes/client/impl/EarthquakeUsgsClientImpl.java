package edu.itserulik.earthquakes.client.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import edu.itserulik.earthquakes.client.EarthquakeUsgsClient;
import edu.itserulik.earthquakes.common.exception.HttpClientException;
import edu.itserulik.earthquakes.model.Geometry;
import edu.itserulik.earthquakes.model.dto.GeoJson;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Singleton
public class EarthquakeUsgsClientImpl implements EarthquakeUsgsClient {

    private HttpClient httpClient = HttpClient.newHttpClient();
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Geometry.class, new GeometryDeserializer())
            .create();
    private String endpointUrl;

    @Inject
    public EarthquakeUsgsClientImpl(@Named("client.earthquakesLastMonthUrlUrl") String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    @Override
    public GeoJson getEarthquakesLastMonth() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(endpointUrl))
                .GET()
                .build();
        return performRequestAndParse(request, GeoJson.class);
    }

    <T> T performRequestAndParse(HttpRequest request, Class<T> type) {
        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), type);
        } catch (IOException | InterruptedException e) {
            throw new HttpClientException("Cannot perform http request", e);
        } catch (JsonSyntaxException e) {
            throw new HttpClientException("Server sends invalid data", e);
        }
    }

}
