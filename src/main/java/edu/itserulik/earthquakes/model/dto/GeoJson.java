package edu.itserulik.earthquakes.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class GeoJson {

    private List<FeatureDto> features;
    private MetadataDto metadata;
}
