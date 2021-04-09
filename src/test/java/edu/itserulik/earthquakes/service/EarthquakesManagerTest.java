package edu.itserulik.earthquakes.service;

import edu.itserulik.earthquakes.client.EarthquakeUsgsClient;
import edu.itserulik.earthquakes.model.Geometry;
import edu.itserulik.earthquakes.model.dto.FeatureDto;
import edu.itserulik.earthquakes.model.dto.GeoJson;
import edu.itserulik.earthquakes.service.impl.EarthquakesManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EarthquakesManagerTest {

    @Mock
    private DistanceCalculator distanceCalculator;
    @Mock
    private EarthquakeUsgsClient usgsClient;
    private int allowedSize = 10;
    private EarthquakesManager earthquakesManager;

    @BeforeEach
    void init() {
        earthquakesManager = new EarthquakesManagerImpl(distanceCalculator, usgsClient, allowedSize);
    }

    @Test
    void getFirstNearestPoints_ZeroElements_EmptyMap() {
        var geoJson = new GeoJson();
        var featuresList = new ArrayList<FeatureDto>();
        geoJson.setFeatures(featuresList);

        when(usgsClient.getEarthquakesLastMonth()).thenReturn(geoJson);
        var distanceFeatureMap = earthquakesManager.getFirstNearestPoints(Geometry.builder().build());

        assertEquals(0, distanceFeatureMap.size());
        verifyNoMoreInteractions(distanceCalculator);
    }

    @Test
    void getFirstNearestPoints_DuplicatedDistance_DeduplicatedMap() {
        var geoJson = new GeoJson();
        var featuresList = new ArrayList<FeatureDto>();
        IntStream.range(0, allowedSize + 1)
                .forEach(i -> {
                    var geo = Geometry.builder().longitude(2.0).latitude(2.0).build();
                    var feature = new FeatureDto();
                    feature.setGeometry(geo);
                    featuresList.add(feature);
                });
        geoJson.setFeatures(featuresList);

        when(usgsClient.getEarthquakesLastMonth()).thenReturn(geoJson);
        when(distanceCalculator.haversinMeters(any(), any())).thenReturn(555.0);
        var distanceFeatureMap = earthquakesManager.getFirstNearestPoints(Geometry.builder().build());

        assertEquals(1, distanceFeatureMap.size());
        assertEquals(555, distanceFeatureMap.firstKey().getDistance());
        assertEquals(555, distanceFeatureMap.lastKey().getDistance());
    }

    @Test
    void getFirstNearestPoints_SixTeenElements_TenEntries() {
        var geoJson = new GeoJson();
        var featuresList = new ArrayList<FeatureDto>();
        IntStream.range(0, allowedSize + 1)
                .forEach(i -> {
                    var geo = Geometry.builder().longitude(2.0).latitude(2.0).build();
                    var feature = new FeatureDto();
                    feature.setGeometry(geo);
                    featuresList.add(feature);
                });
        geoJson.setFeatures(featuresList);

        when(usgsClient.getEarthquakesLastMonth()).thenReturn(geoJson);
        var distance = new AtomicInteger(allowedSize + 2);
        when(distanceCalculator.haversinMeters(any(), any())).then(a -> (double) distance.decrementAndGet());
        var distanceFeatureMap = earthquakesManager.getFirstNearestPoints(Geometry.builder().build());

        assertEquals(allowedSize, distanceFeatureMap.size());
        assertEquals(1, distanceFeatureMap.firstKey().getDistance());
        assertEquals(10, distanceFeatureMap.lastKey().getDistance());
    }

    @Test
    void getFirstNearestPoints_TwoGeo_Sorted() {
        var geoJson = new GeoJson();
        var featuresList = new ArrayList<FeatureDto>();
        var geo = geometry(10, 10);
        var geo2 = geometry(20, 20);
        featuresList.add(featureDto(geo));
        featuresList.add(featureDto(geo2));
        geoJson.setFeatures(featuresList);

        when(usgsClient.getEarthquakesLastMonth()).thenReturn(geoJson);
        when(distanceCalculator.haversinMeters(any(), eq(geo))).thenReturn(20.0);
        when(distanceCalculator.haversinMeters(any(), eq(geo2))).thenReturn(10.0);
        var distanceFeatureMap = earthquakesManager.getFirstNearestPoints(Geometry.builder().build());

        assertEquals(2, distanceFeatureMap.size());
        assertEquals(10.0, distanceFeatureMap.firstKey().getDistance());
        assertEquals(20.0, distanceFeatureMap.lastKey().getDistance());
    }

    @Test
    void getFirstNearestPoints_TwoGeo_TwoResults() {
        var geoJson = new GeoJson();
        var featuresList = new ArrayList<FeatureDto>();
        var geo = geometry(10, 10);
        var geo2 = geometry(20, 20);
        featuresList.add(featureDto(geo));
        featuresList.add(featureDto(geo2));
        geoJson.setFeatures(featuresList);

        when(usgsClient.getEarthquakesLastMonth()).thenReturn(geoJson);
        when(distanceCalculator.haversinMeters(any(), eq(geo))).thenReturn(10.0);
        when(distanceCalculator.haversinMeters(any(), eq(geo2))).thenReturn(10.0);
        var distanceFeatureMap = earthquakesManager.getFirstNearestPoints(Geometry.builder().build());

        assertEquals(2, distanceFeatureMap.size());
        assertEquals(10.0, distanceFeatureMap.firstKey().getDistance());
        assertEquals(10.0, distanceFeatureMap.lastKey().getDistance());
    }

    private FeatureDto featureDto(Geometry geometry) {
        var featureDto = new FeatureDto();
        featureDto.setGeometry(geometry);
        return featureDto;
    }

    private Geometry geometry(double lat, double lon) {
        return Geometry.builder()
                .latitude(lat)
                .longitude(lon)
                .build();
    }

}
