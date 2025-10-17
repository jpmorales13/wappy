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

package com.wappy.weather.controller;

import com.wappy.weather.exception.BadRequestException;
import com.wappy.weather.dto.Forecast;
import com.wappy.weather.service.GeocodingService;
import com.wappy.weather.service.NwsApiService;
import com.google.maps.model.LatLng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherApplicationController {
    private static final Logger log = LoggerFactory.getLogger(WeatherApplicationController.class);

    private final NwsApiService nwsApiService;
    private final GeocodingService geocodingService;
    private final CacheManager cacheManager;

    public WeatherApplicationController(NwsApiService nwsApiService, GeocodingService geocodingService, CacheManager cacheManager) {
        this.nwsApiService = nwsApiService;
        this.geocodingService = geocodingService;
        this.cacheManager = cacheManager;
    }


    /**
     * Entry method for GET requests to retrieve the weather forecast
     * @param address
     * @return
     * @throws Exception
     */
    @GetMapping("/weather/forecast")
    public ResponseEntity<Forecast> returnWeather(@RequestParam String address) throws Exception {
        if (address.isEmpty()) {
            throw new BadRequestException("Value cannot be empty");
        }
        LatLng geoLocation;
        Forecast forecast;

        // To determine if the response is served from cache
        Cache cache = cacheManager.getCache("LatLng");
        boolean fromCache = (cache != null && cache.get(address) != null);

        geoLocation =  geocodingService.getCoordinatesFromAddress(address);
        forecast = nwsApiService.getGridPointInfo(geoLocation);


        return ResponseEntity.ok()
                .header("X-Cache", fromCache ? "CACHED" : "NOT-CACHED")
                .body(forecast);
    }
}
