package edu.itserulik.earthquakes.client.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.net.http.HttpRequest;
import java.util.concurrent.TimeUnit;

@Singleton
@SuppressWarnings("unchecked")
public class CachedEarthquakeUsgsClientImpl extends EarthquakeUsgsClientImpl {

    private LoadingCache<Pair<HttpRequest, Class>, Object> cache;

    @Inject
    public CachedEarthquakeUsgsClientImpl(@Named("client.earthquakesLastMonthUrlUrl") String endpointUrl,
                                          @Named("cache.expireInSec") Integer expireInSec,
                                          @Named("cache.refreshInSec") Integer refreshInSec) {
        super(endpointUrl);
        cache = Caffeine.newBuilder()
                .expireAfterWrite(expireInSec, TimeUnit.SECONDS)
                .refreshAfterWrite(refreshInSec, TimeUnit.SECONDS)
                .build(k -> super.performRequestAndParse(k.getKey(), k.getValue()));
    }

    @Override
    <T> T performRequestAndParse(HttpRequest request, Class<T> type) {
        return (T) cache.get(Pair.of(request, type));
    }
}