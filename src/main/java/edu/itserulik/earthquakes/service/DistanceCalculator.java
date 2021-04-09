package edu.itserulik.earthquakes.service;

import edu.itserulik.earthquakes.model.Geometry;

public interface DistanceCalculator {

    double haversinMeters(Geometry geometry1, Geometry geometry2);

}
