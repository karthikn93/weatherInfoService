package com.weather.weatherinfoservice.services;

import com.weather.weatherinfoservice.exceptions.CityAlreadyExistException;
import com.weather.weatherinfoservice.exceptions.CityNotFoundException;
import com.weather.weatherinfoservice.models.WeatherDataRequest;
import com.weather.weatherinfoservice.models.WeatherDataResponse;
import com.weather.weatherinfoservice.repositories.WeatherDataEntity;
import com.weather.weatherinfoservice.repositories.WeatherRepository;
import com.weather.weatherinfoservice.util.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Primary implementation of both {@link WeatherServiceReader} and {@link WeatherServiceWriter}
 * interfaces that provides weather data operations with local storage and external fallback.
 *
 * <p>This service acts as the main weather data provider, first checking local repository
 * and falling back to external services when data is not available locally.
 *
 */
@Slf4j
@Service
@Primary
public class WeatherServiceImpl implements WeatherServiceWriter, WeatherServiceReader {

    private final WeatherRepository weatherRepository;
    private final WeatherServiceReader weatherServiceExternal;
    private final IdGenerator idGenerator;

    /**
     * Constructs a new WeatherServiceImpl with required dependencies.
     *
     * @param weatherRepository the repository for local weather data storage
     * @param weatherServiceMock the external weather service reader for fallback
     * @param idGenerator the ID generator for new weather records
     */
    public WeatherServiceImpl(WeatherRepository weatherRepository, WeatherServiceReader weatherServiceMock, IdGenerator idGenerator) {
        this.weatherRepository = weatherRepository;
        this.weatherServiceExternal = weatherServiceMock;
        this.idGenerator = idGenerator;
    }

    /**
     * {@inheritDoc}
     *
     * <p><strong>Implementation Strategy:</strong>
     * <ol>
     *   <li>First attempts to retrieve data from local memory</li>
     *   <li>If not found locally, falls back to external weather service</li>
     *   <li>Logs warnings when external fallback is used</li>
     *   <li>Throws exception if data is not found in any source</li>
     * </ol>
     *
     * @throws CityNotFoundException if weather data is not found in local storage or external sources
     */
    @Override
    public WeatherDataResponse getWeatherData(String city){
        Optional<WeatherDataEntity> weatherByCity = weatherRepository.findWeatherByCity(city);
        if (weatherByCity.isEmpty()){
            log.warn("weather data for {} not found in the local memory so fetching externally", city);
            Optional<WeatherDataResponse> weatherDataFromExternal = Optional.ofNullable(weatherServiceExternal.getWeatherData(city));
            if (weatherDataFromExternal.isEmpty()){
                throw new CityNotFoundException(city + " data not found in all the sources");
            }
            return weatherDataFromExternal.get();
        }
        return new WeatherDataResponse(weatherByCity.get());
    }

    /**
     * {@inheritDoc}
     *
     * <p><strong>Implementation Details:</strong>
     * <ul>
     *   <li>Validates that the city doesn't already exist in local storage</li>
     *   <li>Generates a unique ID for the new weather record</li>
     *   <li>Saves the data to the local memory</li>
     *   <li>Returns the created weather data with generated ID</li>
     * </ul>
     *
     * @throws CityAlreadyExistException if weather data already exists for the specified city
     * @throws IllegalArgumentException if the generated ID conflicts with existing records
     */
    @Override
    public WeatherDataResponse addWeatherData(WeatherDataRequest weatherDataRequest){
        if (weatherRepository.findWeatherByCity(weatherDataRequest.getCity()).isPresent()){
            throw new CityAlreadyExistException(weatherDataRequest.getCity()+" already exist in memory, try to add it for new city");
        }
        WeatherDataEntity weatherDataEntity = weatherRepository.saveWeather(weatherDataRequest.getCity(), new WeatherDataEntity(idGenerator, weatherDataRequest));
        return new WeatherDataResponse(weatherDataEntity);

    }
    /**
     * {@inheritDoc}
     *
     * <p><strong>Implementation Behavior:</strong>
     * <ul>
     *   <li>Validates that the city exists in local storage before update</li>
     *   <li>Performs a full update of all weather data fields</li>
     *   <li>Maintains the same ID for the updated record</li>
     *   <li>Returns the complete updated weather data</li>
     * </ul>
     *
     * <p><strong>Note:</strong> This operation replaces all existing data for the city.
     *
     * @throws CityNotFoundException if no weather data exists for the specified city
     */
    @Override
    public WeatherDataResponse updateWeatherData(WeatherDataRequest weatherDataRequest){
        if (weatherRepository.findWeatherByCity(weatherDataRequest.getCity()).isEmpty()){
            throw new CityNotFoundException(weatherDataRequest.getCity() + " city not found in memory, try a city already in memory");
        }
        WeatherDataEntity weatherDataEntity = weatherRepository.updateWeather(weatherDataRequest.getCity(), new WeatherDataEntity(idGenerator,weatherDataRequest));
        return new WeatherDataResponse(weatherDataEntity);
    }

    /**
     * {@inheritDoc}
     *
     * <p><strong>Implementation Details:</strong>
     * <ul>
     *   <li>Validates that the city exists in local storage before deletion</li>
     *   <li>Permanently removes weather data from local repository</li>
     *   <li>Provides no recovery mechanism for deleted data</li>
     * </ul>
     *
     * <p><strong>Warning:</strong> This operation is irreversible and permanently
     * removes all weather data for the specified city.
     *
     * @throws CityNotFoundException if no weather data exists for the specified city
     */
    @Override
    public void deleteWeatherData(String city){
        if (weatherRepository.findWeatherByCity(city).isEmpty()){
            throw new CityNotFoundException(city + " city not found in memory, try a city already in memory");
        }
        weatherRepository.deleteWeather(city);
    }
}
