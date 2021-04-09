package edu.itserulik.earthquakes.service;

import dagger.Binds;
import dagger.Module;
import edu.itserulik.earthquakes.client.ClientModule;
import edu.itserulik.earthquakes.service.impl.DistanceCalculatorImpl;
import edu.itserulik.earthquakes.service.impl.EarthquakesManagerImpl;

@Module(includes = ClientModule.class)
public interface ServiceModule {

    @Binds
    DistanceCalculator distanceCalculator(DistanceCalculatorImpl impl);

    @Binds
    EarthquakesManager earthquakesManager(EarthquakesManagerImpl impl);

}
