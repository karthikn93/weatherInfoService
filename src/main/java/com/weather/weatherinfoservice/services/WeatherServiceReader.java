package com.weather.weatherinfoservice.services;

import com.weather.weatherinfoservice.exceptions.CityNotFoundException;
import com.weather.weatherinfoservice.models.WeatherDataResponse;

/**
 * Service interface for reading weather data operations.
 * Defines the contract for retrieving weather information from various sources.
 *
 * <p>Implementations of this interface are responsible for:
 * <ul>
 *   <li>Retrieving weather data from in-memory storage</li>
 *   <li>Fallback to external source lookup when data is not available In memory</li>
 *   <li>Handling data transformation and response formatting</li>
 * </ul>
 *
 */
public interface WeatherServiceReader {

    /**
     * Retrieves weather data for the specified city.
     *
     * <p>This method attempts to fetch weather information from the local memory first.
     * If the data is not available, it may look up external data sources as fall back.
     *
     * @param city the name of the city to retrieve weather data
     * @return {@link WeatherDataResponse} containing the weather information
     * @throws IllegalArgumentException if city parameter is null or empty
     * @throws CityNotFoundException if weather data for the specified city is not found
     *
     * @see WeatherDataResponse
     */
    WeatherDataResponse getWeatherData(String city);
}
