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

package com.wappy.weather.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
