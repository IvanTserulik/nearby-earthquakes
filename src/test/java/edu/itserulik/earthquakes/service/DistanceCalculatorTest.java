package edu.itserulik.earthquakes.service;

import edu.itserulik.earthquakes.model.Geometry;
import edu.itserulik.earthquakes.service.impl.DistanceCalculatorImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistanceCalculatorTest {

    private DistanceCalculator calculator = new DistanceCalculatorImpl();

    @Test
    void haversinMeters_SameCity_Zero() {
        Geometry newYork = Geometry.builder().latitude(40.730610).longitude(-73.935242).build();
        Geometry secondCity = Geometry.builder().latitude(40.730610).longitude(-73.935242).build();

        double meters = calculator.haversinMeters(newYork, secondCity);

        assertEquals(0, meters);
    }

    @Test
    void haversinMeters_NewYorkLondon_Correct() {
        Geometry newYork = Geometry.builder().latitude(40.730610).longitude(-73.935242).build();
        Geometry london = Geometry.builder().latitude(51.507351).longitude(-0.127758).build();

        double meters = calculator.haversinMeters(newYork, london);

        assertEquals(5579338, Math.round(meters));
    }

    @Test
    void haversinMeters_SydneyLondon_Correct() {
        Geometry sydney = Geometry.builder().latitude(-33.868820).longitude(151.209290).build();
        Geometry london = Geometry.builder().latitude(51.507351).longitude(-0.127758).build();

        double meters = calculator.haversinMeters(london, sydney);

        assertEquals(16989297, Math.round(meters));
    }
}
