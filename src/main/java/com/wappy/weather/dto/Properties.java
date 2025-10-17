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

package com.wappy.weather.dto;

import java.util.List;

public class Properties {
    String generatedAt;
    private List<Period> periods;

    public List<Period> getPeriods() {
        return periods;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }
}
