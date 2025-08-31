package com.weather.weatherinfoservice.services;

import com.weather.weatherinfoservice.exceptions.CityNotFoundException;
import com.weather.weatherinfoservice.models.WeatherDataRequest;
import com.weather.weatherinfoservice.models.WeatherDataResponse;
import com.weather.weatherinfoservice.exceptions.CityAlreadyExistException;

/**
 * Service interface for writing weather data operations.
 * Defines the contract for creating, updating, and deleting weather information.
 *
 * <p>Implementations of this interface are responsible for:
 * <ul>
 *   <li>Validating incoming weather data</li>
 *   <li>Saving weather information to storage</li>
 *   <li>Handling data conflicts and duplicates</li>
 * </ul>
 */
public interface WeatherServiceWriter {

    /**
     * Adds new weather data for a city.
     *
     * <p>Creates a new weather record in the memory only if it not exist already. The implementation should
     * validate all required fields and ensure data consistency.
     *
     * <p><strong>Validation Rules:</strong>
     * <ul>
     *   <li>City name must be unique</li>
     *   <li>Temperature must be a valid numeric value</li>
     *   <li>Unit must be either 'C' (Celsius) or 'F' (Fahrenheit)</li>
     *   <li>Weather conditions must be non-empty</li>
     * </ul>
     *
     * @param weatherDataRequest the weather data to be added
     * @throws IllegalArgumentException if request data is invalid
     * @throws CityAlreadyExistException if weather data already exists for the city
     *
     * @see WeatherDataRequest
     */
    WeatherDataResponse addWeatherData(WeatherDataRequest weatherDataRequest);

    /**
     * Updates existing weather data for a city.
     *
     * <p>Modifies the weather information for an existing city record.
     * All fields in the request will update the existing record.
     *
     * <p><strong>Note:</strong> This is a full update operation, not a partial update.
     * Missing fields will be set to null or default values.
     *
     * @param weatherDataRequest the updated weather data
     * @return {@link WeatherDataResponse} containing the updated weather information
     * @throws IllegalArgumentException if request data is invalid
     * @throws CityNotFoundException if no weather data exists for the specified city
     *
     * @see WeatherDataResponse
     */
    WeatherDataResponse updateWeatherData(WeatherDataRequest weatherDataRequest);

    /**
     * Deletes weather data for a specific city.
     *
     * <p>Removes all weather information for the specified city from the memory.
     * This operation is irreversible and should be used with caution.
     *
     * <p><strong>Idempotency:</strong>
     * Multiple calls to delete the same city should not throw exceptions
     * after the first successful deletion.
     *
     * @param city the name of the city to delete weather data for
     * @throws IllegalArgumentException if city parameter is null or empty
     * @throws CityNotFoundException if no weather data exists for the specified city
     */
    void deleteWeatherData(String city);
}
