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

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
