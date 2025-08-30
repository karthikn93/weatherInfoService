package com.weather.weatherinfoservice.services;

import com.weather.weatherinfoservice.models.WeatherDataRequest;
import com.weather.weatherinfoservice.models.WeatherDataResponse;

public interface WeatherServiceWriter {

    WeatherDataResponse addWeatherData(WeatherDataRequest weatherDataRequest);

    WeatherDataResponse updateWeatherData(WeatherDataRequest weatherDataRequest);

    void deleteWeatherData(String city);
}
