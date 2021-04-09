package edu.itserulik.earthquakes.service.impl;

import edu.itserulik.earthquakes.client.EarthquakeUsgsClient;
import edu.itserulik.earthquakes.model.CalculatedGeometry;
import edu.itserulik.earthquakes.model.Geometry;
import edu.itserulik.earthquakes.model.dto.FeatureDto;
import edu.itserulik.earthquakes.service.DistanceCalculator;
import edu.itserulik.earthquakes.service.EarthquakesManager;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

@Singleton
public class EarthquakesManagerImpl implements EarthquakesManager {

    private DistanceCalculator distanceCalculator;
    private EarthquakeUsgsClient usgsClient;
    private int allowedSize;

    @Inject
    public EarthquakesManagerImpl(DistanceCalculator distanceCalculator,
                                  EarthquakeUsgsClient usgsClient,
                                  @Named("console.printSize") int allowedSize) {
        this.distanceCalculator = distanceCalculator;
        this.usgsClient = usgsClient;
        this.allowedSize = allowedSize;
    }

    @Override
    public NavigableMap<CalculatedGeometry, FeatureDto> getFirstNearestPoints(Geometry point) {
        return getFirstNearestPoints(point, usgsClient.getEarthquakesLastMonth().getFeatures());
    }

    private NavigableMap<CalculatedGeometry, FeatureDto> getFirstNearestPoints(Geometry point, List<FeatureDto> features) {
        var distanceFeatureMap = new TreeMap<CalculatedGeometry, FeatureDto>(
                Comparator.comparing(CalculatedGeometry::getDistance)
                        .thenComparing(g -> g.getGeometry().getLatitude())
                        .thenComparing(g -> g.getGeometry().getLongitude()));
        features.forEach(featureDto -> {
            var currentDistance = distanceCalculator.haversinMeters(point, featureDto.getGeometry());
            if (distanceFeatureMap.size() >= allowedSize) {
                var maxDistance = distanceFeatureMap.lastKey().getDistance();
                if (maxDistance > currentDistance) {
                    distanceFeatureMap.pollLastEntry();
                    distanceFeatureMap.put(CalculatedGeometry.builder()
                                    .distance(currentDistance)
                                    .geometry(featureDto.getGeometry())
                                    .build(),
                            featureDto);
                }
            } else {
                distanceFeatureMap.put(CalculatedGeometry.builder()
                        .distance(currentDistance)
                        .geometry(featureDto.getGeometry())
                        .build(), featureDto);
            }
        });
        return distanceFeatureMap;
    }
}
