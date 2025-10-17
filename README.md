# Wappy Weather Backend

This is the **backend service** for the Wappy Weather application.  
It is a Spring Boot application that integrates with the [National Weather Service API](https://www.weather.gov/documentation/services-web-api) (`api.weather.gov`) to retrieve weather forecasts, enrich them with location data, and serve them in a simplified JSON format.

---

## 🚀 Features
- Accepts a human-readable address as input (`?address=...`).
- Geocodes the address to coordinates (latitude/longitude).
- Calls the National Weather Service (NWS) forecast API.
- Normalizes the forecast into simplified Java DTOs.
- Supports caching with Redis for repeated queries.

---

## 📦 Running the Backend

### Prerequisites
- Java 17+  
- Maven 3.9+  
- (Optional) Redis running locally for caching  

### Run locally
```bash
./mvnw spring-boot:run
```

The application will start at:

```
http://localhost:8080
```

---

## 🔗 API Endpoints

### Get Weather Forecast
Retrieve forecast data for a given address.

**Request:**
```
GET /weather/forecast?address={address}
```

**Example:**
```
GET http://localhost:8080/weather/forecast?address=11901%20Summer%20Springs%20Dr.%20Frisco%20TX%2075036
```

**Response (simplified):**
```json
{
  "address": "11901 Summer Springs Dr. Frisco TX 75036",
  "periods": [
    {
      "number": 1,
      "name": "This Afternoon",
      "startTime": "2025-10-16T17:00:00-05:00",
      "endTime": "2025-10-16T18:00:00-05:00",
      "isDaytime": true,
      "temperature": 84,
      "temperatureUnit": "F",
      "shortForecast": "Partly Sunny"
    },
    {
      "number": 2,
      "name": "Tonight",
      "startTime": "2025-10-16T18:00:00-05:00",
      "endTime": "2025-10-17T06:00:00-05:00",
      "isDaytime": false,
      "temperature": 67,
      "temperatureUnit": "F",
      "shortForecast": "Mostly Cloudy"
    }
    // ...
  ]
}
```

---

## 🗂 Data Model

The backend deserializes the NWS API JSON into Java DTOs for easier handling:

### `ForecastResponse`
Represents the overall forecast result.
- `address` — The user-entered address.
- `periods` — A list of forecast periods.

### `Period`
Represents one time block in the forecast.
- `number` — Sequence number in forecast.
- `name` — Label like *"Tonight"* or *"Saturday"*.
- `startTime` / `endTime` — ISO-8601 timestamps.
- `isDaytime` — `true`/`false`.
- `temperature` — Numeric temperature value.
- `temperatureUnit` — `"F"` or `"C"`.
- `shortForecast` — Short text summary.
- `detailedForecast` — Longer descriptive forecast.
- `icon` — URL for a representative weather icon.

---

## 🧩 Caching

The backend uses Spring’s `@Cacheable` abstraction with Redis to cache forecast responses by address.  

- First request for an address → goes to the NWS API.  
- Subsequent requests for the same address → served from cache until expiration.  

---

## 🧪 Testing the API

You can test using **Postman** or curl:

```bash
curl "http://localhost:8080/weather/forecast?address=11901 Summer Springs Dr. Frisco TX 75036"
```

Or using Postman’s *Params* tab:
- Key: `address`  
- Value: `11901 Summer Springs Dr. Frisco TX 75036`  

---

## ⚙️ Configuration

### `application.properties`
- `server.port` → Backend HTTP port (default `8080`)  
- `spring.redis.host` → Redis host (default `localhost`)  
- `spring.redis.port` → Redis port (default `6379`)  

---

## 📊 Request Flow Diagram

```
[ User / Frontend ]
         |
         v
 [ Wappy Backend ]
   (Spring Boot)
         |
         v
[ National Weather Service API ]
         |
         v
 [ Forecast DTOs ]
         |
         v
 [ JSON Response ]
```

---

## 📜 License
Copyright (c) 2025 Juan Morales

All rights reserved. This software and its source code are the property of Juan Morales.
Unauthorized copying, modification, distribution, or use of this software, via any medium,
is strictly prohibited without prior written permission and a valid commercial license.

To obtain a license, please contact: [morales.juan.pablo@gmail.com].

