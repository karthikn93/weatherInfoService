package com.weather.weatherinfoservice.controllers;

import com.weather.weatherinfoservice.models.WeatherDataRequest;
import com.weather.weatherinfoservice.models.WeatherDataResponse;
import com.weather.weatherinfoservice.services.WeatherServiceReader;
import com.weather.weatherinfoservice.services.WeatherServiceWriter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for managing weather data operations.
 * Provides endpoints for retrieving, adding, updating, and deleting weather information
 * for various cities.
 *
 * <p>This controller serves as the main entry point for weather data operations,
 * delegating business logic to specialized service classes {@link WeatherServiceReader}
 * and {@link WeatherServiceWriter}.
 *
 * <p>All endpoints support validation of input parameters and request bodies
 * using Jakarta Bean Validation annotations.
 *
 * @see WeatherServiceReader
 * @see WeatherServiceWriter
 * @see WeatherDataRequest
 * @see WeatherDataResponse
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/weather")
public class WeatherController {


    private final WeatherServiceReader weatherServiceReader;
    private final WeatherServiceWriter weatherServiceWriter;

    /**
     * Retrieves weather data for a specific city.
     *
     * <p>This endpoint fetches weather information from the service layer
     * based on the provided city name. The city parameter is validated
     * for correctness before processing.
     *
     * <p><strong>Example Usage:</strong>
     * <pre>
     * GET /weather?city=Auckland
     * </pre>
     *
     * @param city the name of the city to retrieve weather data for (required)
     * @return ResponseEntity containing {@link WeatherDataResponse} with HTTP 200 status
     * @throws jakarta.validation.ConstraintViolationException if city parameter is invalid
     * @throws com.weather.weatherinfoservice.exceptions.CityNotFoundException if city is not found
     *
     * @see WeatherServiceReader#getWeatherData(String)
     */
    @GetMapping
    public ResponseEntity<WeatherDataResponse> getWeatherData(@Valid @RequestParam String city) {
        WeatherDataResponse retrievedData = weatherServiceReader.getWeatherData(city);
        return ResponseEntity.ok(retrievedData);
    }

    /**
     * Adds new weather data for a city.
     *
     * <p>This endpoint creates a new weather data entry in the memory.
     * The request body must contain valid weather information including
     * city name, temperature, unit, and weather conditions.
     *
     * <p><strong>Example Usage:</strong>
     * <pre>
     * POST /weather
     * Content-Type: application/json
     *
     * {
     *   "city": "Queenstown",
     *   "temperature": "18",
     *   "unit": "C",
     *   "conditions": "sunny"
     * }
     * </pre>
     *
     * @param weatherDataRequest the weather data to be added (required, validated)
     * @return ResponseEntity with success status and HTTP 201 (Created) status
     * @throws jakarta.validation.ConstraintViolationException if request body is invalid
     * @throws com.weather.weatherinfoservice.exceptions.CityAlreadyExistException if city already exists
     *
     * @see WeatherServiceWriter#addWeatherData(WeatherDataRequest)
     */
    @PostMapping
    public ResponseEntity<Map<String, Boolean>> addWeatherData(@Valid @RequestBody WeatherDataRequest weatherDataRequest) {
        weatherServiceWriter.addWeatherData(weatherDataRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", true));
    }

    /**
     * Updates existing weather data for a city.
     *
     * <p>This endpoint modifies weather information for an existing city.
     * The request body must contain the city name and updated weather data.
     *
     * <p><strong>Example Usage:</strong>
     * <pre>
     * PUT /weather
     * Content-Type: application/json
     *
     * {
     *   "city": "Wellington",
     *   "temperature": "18",
     *   "unit": "C",
     *   "conditions": "rainy"
     * }
     * </pre>
     *
     * @param weatherDataRequest the updated weather data (required, validated)
     * @return ResponseEntity containing updated {@link WeatherDataResponse} with HTTP 200 status
     * @throws jakarta.validation.ConstraintViolationException if request body is invalid
     * @throws com.weather.weatherinfoservice.exceptions.CityNotFoundException if city is not found
     *
     * @see WeatherServiceWriter#updateWeatherData(WeatherDataRequest)
     */
    @PutMapping
    public ResponseEntity<WeatherDataResponse> updateWeatherData(@Valid @RequestBody WeatherDataRequest weatherDataRequest) {
        WeatherDataResponse updatedData = weatherServiceWriter.updateWeatherData(weatherDataRequest);
        return ResponseEntity.ok(updatedData);
    }

    /**
     * Deletes weather data for a specific city.
     *
     * <p>This endpoint removes all weather information for the specified city
     * from the memory. The city parameter is validated before processing.
     *
     * <p><strong>Example Usage:</strong>
     * <pre>
     * DELETE /weather?city=Christchurch
     * </pre>
     *
     * @param city the name of the city to delete weather data for (required)
     * @return ResponseEntity with no content and HTTP 204 (No Content) status
     * @throws jakarta.validation.ConstraintViolationException if city parameter is invalid
     * @throws com.weather.weatherinfoservice.exceptions.CityNotFoundException if city is not found
     *
     * @see WeatherServiceWriter#deleteWeatherData(String)
     */
    @DeleteMapping
    public ResponseEntity<WeatherDataResponse> deleteWeatherData(@Valid @RequestParam String city) {
        weatherServiceWriter.deleteWeatherData(city);
        return ResponseEntity.noContent().build();
    }

    /**
     * Health check endpoint for service monitoring.
     *
     * <p>This endpoint provides a simple way to verify that the weather service
     * is running and responsive. It returns a plain text response indicating
     * service status.
     *
     * <p><strong>Example Usage:</strong>
     * <pre>
     * GET /weather/health
     * </pre>
     *
     * @return ResponseEntity with health status message and HTTP 200 status
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Weather service is running");
    }
}
