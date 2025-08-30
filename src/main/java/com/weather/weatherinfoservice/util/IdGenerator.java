package com.weather.weatherinfoservice.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGenerator {

    public UUID generateId() {
        return UUID.randomUUID();
    }
}
