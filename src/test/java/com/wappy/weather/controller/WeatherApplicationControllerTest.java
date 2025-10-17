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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherApplicationController.class)
class WeatherApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeocodingService geocodingService;

    @MockBean
    private NwsApiService nwsApiService;

    @MockBean
    private CacheManager cacheManager;

    @InjectMocks
    private WeatherApplicationController controller;

    private Forecast forecast;
    private LatLng latLng;
    private String validAddress = "3415 W Main St, Frisco, TX 75034";
    private String emptyAddress = "";

    @BeforeEach
    void setUp() {
        forecast = new Forecast();
        latLng = new LatLng(45.0, -90.0);
    }

    @Test
    void testEmptyAddressRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/weather/forecast")
                        .param("address", emptyAddress))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException));
    }

    @Test
    void testNullAddressRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/weather/forecast")
                        .param("address", ""))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException));
    }
}
