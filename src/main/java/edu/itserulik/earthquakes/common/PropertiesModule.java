package edu.itserulik.earthquakes.common;

import dagger.Module;
import dagger.Provides;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.io.IOException;
import java.util.Properties;

@Module
@Slf4j
public class PropertiesModule {

    @Provides
    Properties properties() {
        var conf = "/application.properties";
        var props = new Properties();
        try {
            props.load(getClass().getResourceAsStream(conf));
            props.putAll(System.getProperties());
        } catch (IOException e) {
            log.error("Could not load config: ", e);
            throw new InternalError("Could not load config " + conf);
        }
        return props;
    }

    @Provides
    @Named("console.printSize")
    Integer allowedSize(Properties props) {
        return Integer.valueOf(props.getProperty("console.printSize"));
    }

    @Provides
    @Named("client.earthquakesLastMonthUrlUrl")
    String lastMonthUrlUrl(Properties props) {
        return props.getProperty("client.earthquakesLastMonthUrlUrl");
    }

    @Provides
    @Named("cache.expireInSec")
    Integer cacheExpire(Properties props) {
        return Integer.valueOf(props.getProperty("cache.expireInSec"));
    }

    @Provides
    @Named("cache.refreshInSec")
    Integer cacheRefresh(Properties props) {
        return Integer.valueOf(props.getProperty("cache.refreshInSec"));
    }

    @Provides
    @Named("cache.enabled")
    Boolean cacheEnabled(Properties props) {
        return Boolean.valueOf(props.getProperty("cache.enabled"));
    }
}