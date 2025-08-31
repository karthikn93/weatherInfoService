package com.weather.weatherinfoservice.services;

import com.weather.weatherinfoservice.exceptions.CityNotFoundException;
import com.weather.weatherinfoservice.models.WeatherDataResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

/**
 * Mock implementation of {@link WeatherServiceReader} that simulates an external weather data source.
 *
 * <p>This service provides pre-defined weather data for various New Zealand cities
 * and acts as a fallback when data is not available in local storage.
 *
 * <p><strong>Note:</strong> This is a simulation class and does not make actual
 * external API calls. It serves as a stub for development and testing purposes.
 *
 * @see WeatherServiceReader
 */
@Service
public class WeatherDataExternalSource implements WeatherServiceReader {

    /**
     * Pre-defined weather data for various New Zealand cities.
     * Contains sample data for demonstration and fallback purposes.
     */
    private static final Map<String, WeatherDataResponse> weatherMap = Map.ofEntries(
            Map.entry("Hamilton", new WeatherDataResponse(UUID.randomUUID(), "Hamilton", "11", "C", "sunny", LocalDate.now())),
            Map.entry("Tauranga", new WeatherDataResponse(UUID.randomUUID(), "Tauranga", "19", "C", "sunny", LocalDate.now())),
            Map.entry("Napier-Hastings", new WeatherDataResponse(UUID.randomUUID(), "Napier-Hastings", "17", "C", "rainy", LocalDate.now())),
            Map.entry("Dunedin", new WeatherDataResponse(UUID.randomUUID(), "Dunedin", "12", "C", "cloudy", LocalDate.now())),
            Map.entry("Palmerston North", new WeatherDataResponse(UUID.randomUUID(), "Palmerston North", "15", "C", "windy", LocalDate.now())),
            Map.entry("Nelson", new WeatherDataResponse(UUID.randomUUID(), "Nelson", "18", "C", "sunny", LocalDate.now())),
            Map.entry("Rotorua", new WeatherDataResponse(UUID.randomUUID(), "Rotorua", "16", "C", "rainy", LocalDate.now())),
            Map.entry("New Plymouth", new WeatherDataResponse(UUID.randomUUID(), "New Plymouth", "17", "C", "sunny", LocalDate.now())),
            Map.entry("Whangarei", new WeatherDataResponse(UUID.randomUUID(), "Whangārei", "18", "C", "sunny", LocalDate.now()))
    );

    /**
     * {@inheritDoc}
     *
     * <p><strong>Implementation Details:</strong>
     * This implementation returns pre-defined weather data from a static map
     * containing sample data for various New Zealand cities.
     *
     * <p><strong>Supported Cities:</strong>
     * <ul>
     *   <li>Hamilton</li>
     *   <li>Tauranga</li>
     *   <li>Napier-Hastings</li>
     *   <li>Dunedin</li>
     *   <li>Palmerston North</li>
     *   <li>Nelson</li>
     *   <li>Rotorua</li>
     *   <li>New Plymouth</li>
     *   <li>Whangarei</li>
     * </ul>
     *
     * <p><strong>Note:</strong> For cities not in the pre-defined list, this method
     * returns null. The calling service should handle null responses
     * appropriately by throwing {@link CityNotFoundException}.
     *
     * @param city the name of the city to retrieve weather data for
     * @return WeatherDataResponse for the requested city, or null if the city
     *         is not in the pre-defined list
     *
     * @see WeatherServiceReader#getWeatherData(String)
     */
    @Override
    public WeatherDataResponse getWeatherData(String city) {
        return weatherMap.get(city);

    }
}