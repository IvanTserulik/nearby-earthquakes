package edu.itserulik.earthquakes.client;

import dagger.Module;
import dagger.Provides;
import edu.itserulik.earthquakes.client.impl.CachedEarthquakeUsgsClientImpl;
import edu.itserulik.earthquakes.client.impl.EarthquakeUsgsClientImpl;
import edu.itserulik.earthquakes.common.PropertiesModule;

import javax.inject.Named;

@Module(includes = PropertiesModule.class)
public class ClientModule {

    @Provides
    EarthquakeUsgsClient earthquakeUsgsClient(@Named("cache.enabled") Boolean cacheEnabled,
                                              CachedEarthquakeUsgsClientImpl cachedImpl,
                                              EarthquakeUsgsClientImpl impl) {
        return cacheEnabled ? cachedImpl : impl;
    }
}
