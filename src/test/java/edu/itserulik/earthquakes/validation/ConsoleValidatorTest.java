package edu.itserulik.earthquakes.validation;

import edu.itserulik.earthquakes.model.Geometry;
import edu.itserulik.earthquakes.validation.impl.ConsoleValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsoleValidatorTest {

    private ValidationModule validationModule = new ValidationModule();
    private ConsoleValidator consoleValidator = new ConsoleValidatorImpl(validationModule.validator());

    @Test
    void validate_ValidGeometry_NoConstraints() {
        Geometry newYork = Geometry.builder().latitude(40.730610).longitude(-73.935242).build();

        Set<ConstraintViolation<Geometry>> meters = consoleValidator.validate(newYork);

        assertEquals(0, meters.size());
    }

    @ParameterizedTest(name = "{index} => lat={0}, lon={1}, field={2}, errorMessage={3}")
    @CsvSource({
            "400, -73, latitude, must be less than or equal to 90",
            "-400, -73, latitude, must be greater than or equal to -90",
            "-45, -400, longitude, must be greater than or equal to -180",
            "-45, 400, longitude, must be less than or equal to 180",
    })
    void validate_InvalidGeometry_Constraint(double lat, double lon, String field, String errorMessage) {
        Geometry geometry = Geometry.builder().latitude(lat).longitude(lon).build();

        Set<ConstraintViolation<Geometry>> meters = consoleValidator.validate(geometry);

        assertEquals(1, meters.size());
        assertEquals(field, meters.iterator().next().getPropertyPath().toString());
        assertEquals(errorMessage, meters.iterator().next().getMessage());
    }

    @Test
    void validate_InvalidGeometry_TwoConstraints() {
        Geometry newYork = Geometry.builder().latitude(-400.0).longitude(400.0).build();

        Set<ConstraintViolation<Geometry>> meters = consoleValidator.validate(newYork);

        assertEquals(2, meters.size());
    }
}
