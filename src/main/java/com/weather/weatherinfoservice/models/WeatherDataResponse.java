package com.weather.weatherinfoservice.models;

import com.weather.weatherinfoservice.repositories.WeatherDataEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherDataResponse {

    private UUID uuid;
    private String city;
    private String temp;
    private String unit;
    private String weather;
    private LocalDate date;

    public WeatherDataResponse(WeatherDataEntity weatherDataEntity) {
        this.uuid = weatherDataEntity.getId();
        this.city = weatherDataEntity.getCity();
        this.temp = weatherDataEntity.getTemp();
        this.unit = weatherDataEntity.getUnit();
        this.weather = weatherDataEntity.getWeather();
        this.date = weatherDataEntity.getDate();
    }

}
