package edu.itserulik.earthquakes.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
public class Geometry {

    @Min(-180)
    @Max(180)
    private Double longitude;
    @Min(-90)
    @Max(90)
    private Double latitude;
}
