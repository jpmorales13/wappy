/**
 * Wappy Weather Backend
 *
 * Description: Provides weather forecast services by querying the
 * National Weather Service API and caching results in Redis.
 *
 * @author Juan Morales
 * @version 1.0
 * @since 2025-10-17
 */
package com.apple.weather.service;

import com.apple.weather.exception.ResourceNotFoundException;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class GeocodingService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    private GeoApiContext context;

    @PostConstruct
    public void initialize() {
        this.context = new GeoApiContext.Builder().apiKey(apiKey).build();
    }

    /**
     *  This method retrieves latitude and longitude given an address from google maps
     * @param address
     * @return
     * @throws Exception
     */
    @Cacheable(value = "LatLng", key = "#address")
    public LatLng getCoordinatesFromAddress(String address) throws Exception {
        GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
        if (results != null && results.length > 0) {
            return results[0].geometry.location;
        }
        throw new ResourceNotFoundException("Could not find coordinates for the given address.");
    }
}

