package edu.itserulik.earthquakes.console.impl;

import edu.itserulik.earthquakes.common.exception.UserInputException;
import edu.itserulik.earthquakes.console.ConsoleHandler;
import edu.itserulik.earthquakes.model.Geometry;
import edu.itserulik.earthquakes.service.EarthquakesManager;
import edu.itserulik.earthquakes.validation.ConsoleValidator;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Singleton
@Slf4j
public class ConsoleHandlerImpl implements ConsoleHandler {

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private ConsoleValidator consoleValidator;
    private EarthquakesManager earthquakesManager;

    @Inject
    public ConsoleHandlerImpl(ConsoleValidator consoleValidator,
                              EarthquakesManager earthquakesManager) {
        this.consoleValidator = consoleValidator;
        this.earthquakesManager = earthquakesManager;
    }

    @Override
    public void handleUserData() {
        try {
            readUserDataAndProcess();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    private void readUserDataAndProcess() {
        var point = scanCoordinates();
        var constraints = consoleValidator.constraints(point);
        if (!constraints.isEmpty()) {
            log.info(String.join("\n", constraints));
            return;
        }
        earthquakesManager.getFirstNearestPoints(point)
                .forEach((geo, feature) -> {
                    var roundedDistanceInKm = Math.round(geo.getDistance() / 1000);
                    var line = String.join(" || ",
                            feature.getProperties().getTitle(),
                            Long.toString(roundedDistanceInKm));
                    log.info(line);
                });
    }

    private Geometry scanCoordinates() {
        double latitude;
        double longitude;
        try {
            latitude = Double.parseDouble(reader.readLine());
            longitude = Double.parseDouble(reader.readLine());
        } catch (IOException e) {
            throw new UserInputException("Please provide correct coordinate format");
        }
        return Geometry.builder().latitude(latitude).longitude(longitude).build();
    }
}
