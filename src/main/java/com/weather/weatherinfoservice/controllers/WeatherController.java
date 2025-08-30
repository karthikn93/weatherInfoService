package com.weather.weatherinfoservice.controllers;

import com.weather.weatherinfoservice.models.WeatherDataRequest;
import com.weather.weatherinfoservice.models.WeatherDataResponse;
import com.weather.weatherinfoservice.services.WeatherServiceInternal;
import com.weather.weatherinfoservice.services.WeatherServiceReader;
import com.weather.weatherinfoservice.services.WeatherServiceWriter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherServiceReader weatherServiceReader;
    private final WeatherServiceWriter weatherServiceWriter;

    @GetMapping
    public ResponseEntity<WeatherDataResponse> getWeatherData(@Valid @RequestParam String city) {
        WeatherDataResponse retrievedData = weatherServiceReader.getWeatherData(city);
        return ResponseEntity.ok(retrievedData);
    }

    @PostMapping
    public ResponseEntity<Map<String, Boolean>> addWeatherData(@Valid @RequestBody WeatherDataRequest weatherDataRequest) {
        weatherServiceWriter.addWeatherData(weatherDataRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success", true));
    }

    @PutMapping
    public ResponseEntity<WeatherDataResponse> updateWeatherData(@Valid @RequestBody WeatherDataRequest weatherDataRequest) {
        WeatherDataResponse updatedData = weatherServiceWriter.updateWeatherData(weatherDataRequest);
        return ResponseEntity.ok(updatedData);
    }

    @DeleteMapping
    public ResponseEntity<WeatherDataResponse> deleteWeatherData(@Valid @RequestParam String city) {
        weatherServiceWriter.deleteWeatherData(city);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Weather service is running");
    }
}
