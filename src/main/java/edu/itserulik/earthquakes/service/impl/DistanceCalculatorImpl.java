package edu.itserulik.earthquakes.service.impl;

import edu.itserulik.earthquakes.model.Geometry;
import edu.itserulik.earthquakes.service.DistanceCalculator;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DistanceCalculatorImpl implements DistanceCalculator {

    @Inject
    public DistanceCalculatorImpl() {
    }

    @Override
    public double haversinMeters(Geometry geometry1, Geometry geometry2) {
        var geoCalc = new GeodeticCalculator();
        var reference = Ellipsoid.WGS84;
        var pointA = new GlobalPosition(geometry1.getLatitude(), geometry1.getLongitude(), 0.0);
        var pointB = new GlobalPosition(geometry2.getLatitude(), geometry2.getLongitude(), 0.0);
        return geoCalc.calculateGeodeticCurve(reference, pointB, pointA).getEllipsoidalDistance();
    }
}
