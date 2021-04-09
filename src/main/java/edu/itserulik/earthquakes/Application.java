package edu.itserulik.earthquakes;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class Application {

    public static void main(String[] args) {
        var appComponent = DaggerAppComponent.create();
        var consoleHandler = appComponent.consoleHandler();
        var earthquakeUsgsClient = appComponent.earthquakeUsgsClient();
        CompletableFuture.runAsync(earthquakeUsgsClient::getEarthquakesLastMonth);

        while (true) {
            consoleHandler.handleUserData();
        }
    }

}
