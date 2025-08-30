package com.weather.weatherinfoservice.services;

import com.weather.weatherinfoservice.models.WeatherDataResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class WeatherServiceExternal implements WeatherServiceReader {


    @Override
    public WeatherDataResponse getWeatherData(String city) {

        Map<String, WeatherDataResponse> weatherMap = Map.ofEntries(
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

        return weatherMap.get(city);

    }
}