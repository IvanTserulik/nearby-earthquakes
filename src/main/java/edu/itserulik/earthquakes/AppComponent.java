package edu.itserulik.earthquakes;

import dagger.Component;
import edu.itserulik.earthquakes.client.EarthquakeUsgsClient;
import edu.itserulik.earthquakes.console.ConsoleHandler;
import edu.itserulik.earthquakes.console.ConsoleModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = ConsoleModule.class)
public interface AppComponent {

    ConsoleHandler consoleHandler();

    EarthquakeUsgsClient earthquakeUsgsClient();

}
