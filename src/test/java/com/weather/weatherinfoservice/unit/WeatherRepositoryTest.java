package com.weather.weatherinfoservice.unit;

import com.weather.weatherinfoservice.models.WeatherDataRequest;
import com.weather.weatherinfoservice.repositories.WeatherDataEntity;
import com.weather.weatherinfoservice.repositories.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class WeatherRepositoryTest {

    private WeatherRepository weatherRepository;

    @BeforeEach
    public void setup() {
        weatherRepository = new WeatherRepository();
    }

    @Test
    public void shouldGetWeatherForExistingCity() {
        Optional<WeatherDataEntity> weatherDataRequest = weatherRepository.findWeatherByCity("Auckland");
        assert(weatherDataRequest.isPresent());
        assert(weatherDataRequest.get().getCity().equals("Auckland"));
    }

    @Test
    public void shouldSaveWeatherToRepository() {
        WeatherDataEntity mockWeatherDataRequest = new WeatherDataEntity(UUID.randomUUID(), "Hamilton", "12", "C", "sunny", LocalDate.now());
        WeatherDataEntity createdWeatherDataRequest = weatherRepository.saveWeather(mockWeatherDataRequest.getCity(), mockWeatherDataRequest);
        assert(createdWeatherDataRequest.equals(mockWeatherDataRequest));
    }

    @Test
    public void shouldUpdateWeatherToRepository() {
        WeatherDataEntity mockWeatherDataRequest = new WeatherDataEntity(UUID.randomUUID(), "Wellington", "14", "C", "sunny", LocalDate.now());
        WeatherDataEntity updatedWeatherDataRequest = weatherRepository.updateWeather(mockWeatherDataRequest.getCity(), mockWeatherDataRequest);
        assert(updatedWeatherDataRequest.equals(mockWeatherDataRequest));
    }

    @Test
    public void shouldDeleteWeatherFromRepository() {
        weatherRepository.deleteWeather("Auckland");
        assert(weatherRepository.findWeatherByCity("Auckland").isEmpty());
    }
}
