package com.weather.weatherinfoservice.exceptions;

public class CityAlreadyExistException extends RuntimeException {
    public CityAlreadyExistException(String message) {
        super(message);
    }
}
