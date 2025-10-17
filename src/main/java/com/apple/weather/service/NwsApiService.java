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
import com.apple.weather.model.Forecast;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.LatLng;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NwsApiService {

    private final RestTemplate restTemplate;

    public NwsApiService() {
        this.restTemplate = new RestTemplate();
    }


    /**
     * This method uses latitude and longitude to call the national weather service
     * and retrieve the grid information that would be needed to make a call for the
     * weather forecast.
     * @param geoLocation
     * @return
     * @throws JsonProcessingException
     */
    @Cacheable(value = "Forecast", key = "#geoLocation")
    public Forecast getGridPointInfo(LatLng geoLocation) throws JsonProcessingException {
        String url = String.format("https://api.weather.gov/points/%f,%f", geoLocation.lat, geoLocation.lng);
        String gridPointJson = restTemplate.getForObject(url, String.class); // Returns JSON string

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(gridPointJson);

        String office = rootNode.at("/properties/gridId").asText();
        int gridX = rootNode.at("/properties/gridX").asInt();
        int gridY = rootNode.at("/properties/gridY").asInt();

        return getForecast(office, gridX, gridY);
    }

    /**
     * Using the grid information, this method retrieves the Weather Forecast
     * @param office
     * @param gridX
     * @param gridY
     * @return
     * @throws JsonProcessingException
     */
    public Forecast getForecast(String office, int gridX, int gridY) throws JsonProcessingException {
        String url = String.format("https://api.weather.gov/gridpoints/%s/%d,%d/forecast", office, gridX, gridY);
        Forecast forecast = restTemplate.getForObject(url, Forecast.class);

        if (forecast != null && forecast.getProperties().getPeriods().size() > 0) {
            return forecast;
        }

        throw new ResourceNotFoundException("Could not find weather for given coordinates: " + gridX + "," + gridY);
    }
}
