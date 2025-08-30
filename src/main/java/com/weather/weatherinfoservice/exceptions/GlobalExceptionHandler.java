package com.weather.weatherinfoservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<Object> handleCityNotFoundException(CityNotFoundException exception){

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("status", HttpStatus.NOT_FOUND.value());
        log.error(exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(CityAlreadyExistException.class)
    public ResponseEntity<Object> handleCityAlreadyExistException(CityAlreadyExistException exception){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("status", HttpStatus.CONFLICT.value());
        log.error(exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception exception){

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error(exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
