package edu.itserulik.earthquakes.client;

import edu.itserulik.earthquakes.model.dto.GeoJson;

public interface EarthquakeUsgsClient {

    GeoJson getEarthquakesLastMonth();

}
