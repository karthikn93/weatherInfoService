package com.weather.weatherinfoservice.services;

import com.weather.weatherinfoservice.models.WeatherDataResponse;

public interface WeatherServiceReader {

    WeatherDataResponse getWeatherData(String city);
}
