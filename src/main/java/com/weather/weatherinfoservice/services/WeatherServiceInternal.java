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

@Slf4j
@Service
@Primary
public class WeatherServiceInternal implements WeatherServiceWriter, WeatherServiceReader {

    private final WeatherRepository weatherRepository;
    private final WeatherServiceReader weatherServiceExternal;
    private final IdGenerator idGenerator;

    public WeatherServiceInternal(WeatherRepository weatherRepository, WeatherServiceReader weatherServiceMock, IdGenerator idGenerator) {
        this.weatherRepository = weatherRepository;
        this.weatherServiceExternal = weatherServiceMock;
        this.idGenerator = idGenerator;
    }

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

    @Override
    public WeatherDataResponse addWeatherData(WeatherDataRequest weatherDataRequest){
        if (weatherRepository.findWeatherByCity(weatherDataRequest.getCity()).isPresent()){
            throw new CityAlreadyExistException(weatherDataRequest.getCity()+" city already exist in memory, try to add it for new city");
        }
        WeatherDataEntity weatherDataEntity = weatherRepository.saveWeather(weatherDataRequest.getCity(), new WeatherDataEntity(idGenerator, weatherDataRequest));
        return new WeatherDataResponse(weatherDataEntity);

    }

    @Override
    public WeatherDataResponse updateWeatherData(WeatherDataRequest weatherDataRequest){
        if (weatherRepository.findWeatherByCity(weatherDataRequest.getCity()).isEmpty()){
            throw new CityNotFoundException(weatherDataRequest.getCity() + " city not found in memory, try a city already in memory");
        }
        WeatherDataEntity weatherDataEntity = weatherRepository.updateWeather(weatherDataRequest.getCity(), new WeatherDataEntity(idGenerator,weatherDataRequest));
        return new WeatherDataResponse(weatherDataEntity);
    }

    @Override
    public void deleteWeatherData(String city){
        if (weatherRepository.findWeatherByCity(city).isEmpty()){
            throw new CityNotFoundException(city + " city not found in memory, try a city already in memory");
        }
        weatherRepository.deleteWeather(city);
    }
}
