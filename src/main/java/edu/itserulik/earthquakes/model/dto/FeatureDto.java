package edu.itserulik.earthquakes.model.dto;

import edu.itserulik.earthquakes.model.Geometry;
import lombok.Data;

@Data
public class FeatureDto {

    private String id;
    private PropertiesDto properties;
    private Geometry geometry;

}
