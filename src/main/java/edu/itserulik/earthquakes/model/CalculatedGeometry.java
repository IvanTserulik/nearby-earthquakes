package edu.itserulik.earthquakes.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
public class CalculatedGeometry {
    private Geometry geometry;
    @EqualsAndHashCode.Exclude
    private double distance;
}
