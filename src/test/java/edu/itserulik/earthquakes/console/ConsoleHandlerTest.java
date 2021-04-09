package edu.itserulik.earthquakes.console;

import edu.itserulik.earthquakes.console.impl.ConsoleHandlerImpl;
import edu.itserulik.earthquakes.model.Geometry;
import edu.itserulik.earthquakes.service.EarthquakesManager;
import edu.itserulik.earthquakes.validation.ConsoleValidator;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsoleHandlerTest {

    @Mock
    private BufferedReader reader;
    @Mock
    private ConsoleValidator consoleValidator;
    @Mock
    private EarthquakesManager earthquakesManager;
    private ConsoleHandler consoleHandler;

    @BeforeEach
    void init() throws Exception {
        consoleHandler = new ConsoleHandlerImpl(consoleValidator, earthquakesManager);
        FieldUtils.writeField(consoleHandler, "reader", reader, true);
    }

    @Test
    void handleUserData_InvalidLine_NoInvocations() throws IOException {
        when(reader.readLine()).thenReturn("s");

        consoleHandler.handleUserData();

        verifyNoMoreInteractions(consoleValidator);
        verifyNoMoreInteractions(earthquakesManager);
    }

    @Test
    void handleUserData_ValidLine_ClientInvoked() throws IOException {
        var geometry = Geometry.builder().latitude(45.0).longitude(45.0).build();
        var set = Collections.<String>emptySet();

        when(reader.readLine()).thenReturn("45");
        when(consoleValidator.constraints(geometry)).thenReturn(set);
        when(earthquakesManager.getFirstNearestPoints(geometry)).thenReturn(null);
        consoleHandler.handleUserData();

        verify(consoleValidator).constraints(geometry);
        verify(earthquakesManager).getFirstNearestPoints(geometry);
    }

    @Test
    void handleUserData_ValidLineWithConstraint_ClientNotInvoked() throws IOException {
        var geometry = Geometry.builder().latitude(450.0).longitude(450.0).build();
        var set = Collections.singleton("constraint");

        when(reader.readLine()).thenReturn("450");
        when(consoleValidator.constraints(geometry)).thenReturn(set);
        consoleHandler.handleUserData();

        verify(consoleValidator).constraints(geometry);
        verifyNoMoreInteractions(earthquakesManager);
    }
}
