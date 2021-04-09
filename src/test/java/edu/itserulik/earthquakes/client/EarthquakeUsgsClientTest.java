package edu.itserulik.earthquakes.client;

import edu.itserulik.earthquakes.client.impl.CachedEarthquakeUsgsClientImpl;
import edu.itserulik.earthquakes.common.exception.HttpClientException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EarthquakeUsgsClientTest {

    private String endpoint = "http://endpoint.com/";
    private int expireInSec = 60;
    private int refreshInSec = 45;

    @Mock
    private HttpResponse<String> httpResponse;
    @Mock
    private HttpClient httpClient;
    private EarthquakeUsgsClient earthquakeUsgsClient;

    @BeforeEach
    void init() throws IllegalAccessException {
        earthquakeUsgsClient = new CachedEarthquakeUsgsClientImpl(endpoint, expireInSec, refreshInSec);
        FieldUtils.writeField(earthquakeUsgsClient, "httpClient", httpClient, true);
    }

    @Test
    void getEarthquakesLastMonth_OneInvocation_OneHttpCall() throws IOException, InterruptedException {
        when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn("{}");

        earthquakeUsgsClient.getEarthquakesLastMonth();

        verify(httpClient).send(any(), any());
    }

    @Test
    void getEarthquakesLastMonth_DoubleInvocation_OneHttpCall() throws IOException, InterruptedException {
        when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn("{}");

        earthquakeUsgsClient.getEarthquakesLastMonth();
        earthquakeUsgsClient.getEarthquakesLastMonth();

        verify(httpClient).send(any(), any());
    }

    @Test
    void getEarthquakesLastMonth_CannotDeserialize_Exception() throws IOException, InterruptedException {
        when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn("{error}");

        var exception = assertThrows(
                HttpClientException.class,
                () -> earthquakeUsgsClient.getEarthquakesLastMonth()
        );
        assertEquals("Server sends invalid data", exception.getMessage());

        verify(httpClient).send(any(), any());
    }

    @Test
    void getEarthquakesLastMonth_CannotDeserialize_CacheRecovered() throws IOException, InterruptedException {
        when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);
        when(httpResponse.body())
                .thenReturn("{error}")
                .thenReturn("{}");

        assertThrows(
                HttpClientException.class,
                () -> earthquakeUsgsClient.getEarthquakesLastMonth()
        );
        assertDoesNotThrow(() -> earthquakeUsgsClient.getEarthquakesLastMonth());
        verify(httpClient, times(2)).send(any(), any());
    }

}
