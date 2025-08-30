package com.weather.weatherinfoservice.repositories;

import com.weather.weatherinfoservice.models.WeatherDataRequest;
import com.weather.weatherinfoservice.util.IdGenerator;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@Data
public class WeatherDataEntity {

    private final UUID id;
    private final String city;
    private final String temp;
    private final String unit;
    private final String weather;
    private final LocalDate date;

    public WeatherDataEntity(IdGenerator idGenerator, WeatherDataRequest weatherDataRequest) {
        this.id = idGenerator.generateId();
        this.city = weatherDataRequest.getCity();
        this.temp = weatherDataRequest.getTemp();
        this.unit = weatherDataRequest.getUnit();
        this.weather = weatherDataRequest.getWeather();
        this.date = weatherDataRequest.getDate();
    }
}
