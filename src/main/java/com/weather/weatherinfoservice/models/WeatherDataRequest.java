package com.weather.weatherinfoservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class WeatherDataRequest {

    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "temperature is required")
    private String temp;

    @Pattern(regexp = "^(C|F)$", message = "Unit must be either 'C' for celsius or 'F' for Fahrenheit")
    private String unit;

    @PastOrPresent(message = "date can not be in the future")
    private LocalDate date;

    @NotBlank(message = "weather description is required")
    private String weather;
}
