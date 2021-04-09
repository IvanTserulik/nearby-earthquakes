package edu.itserulik.earthquakes.service;

import edu.itserulik.earthquakes.model.CalculatedGeometry;
import edu.itserulik.earthquakes.model.Geometry;
import edu.itserulik.earthquakes.model.dto.FeatureDto;

import java.util.NavigableMap;

public interface EarthquakesManager {

    NavigableMap<CalculatedGeometry, FeatureDto> getFirstNearestPoints(Geometry point);

}
