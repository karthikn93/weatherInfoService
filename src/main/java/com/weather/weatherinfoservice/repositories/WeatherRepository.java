package com.weather.weatherinfoservice.repositories;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class WeatherRepository {

    private final Map<String, WeatherDataEntity> inMemoryWeatherData = new HashMap<>();

    public WeatherRepository() {
        initializeSampleWeatherData();
    }

    private void initializeSampleWeatherData() {
        inMemoryWeatherData.put("Auckland", new WeatherDataEntity(UUID.randomUUID(), "Auckland", "15","C", "rainy", LocalDate.now()));
        inMemoryWeatherData.put("Christchurch", new WeatherDataEntity(UUID.randomUUID(), "Christchurch", "7","C", "Cloudy", LocalDate.now()));
        inMemoryWeatherData.put("Wellington", new WeatherDataEntity(UUID.randomUUID(), "Wellington", "22","C", "sunny", LocalDate.now()));
    }

    public Optional<WeatherDataEntity> findWeatherByCity(String city) {
        return Optional.ofNullable(inMemoryWeatherData.get(city));
    }

    public WeatherDataEntity saveWeather(String city, WeatherDataEntity WeatherDataEntity) {
        inMemoryWeatherData.put(city, WeatherDataEntity);
        return WeatherDataEntity;
    }

    public WeatherDataEntity updateWeather(String city, WeatherDataEntity WeatherDataEntity) {
        inMemoryWeatherData.put(city, WeatherDataEntity);
        return WeatherDataEntity;
    }

    public void deleteWeather(String city) {
        inMemoryWeatherData.remove(city);
    }

}
