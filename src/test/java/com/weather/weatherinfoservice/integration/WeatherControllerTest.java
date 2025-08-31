package com.weather.weatherinfoservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.weatherinfoservice.controllers.WeatherController;
import com.weather.weatherinfoservice.exceptions.CityAlreadyExistException;
import com.weather.weatherinfoservice.exceptions.CityNotFoundException;
import com.weather.weatherinfoservice.models.WeatherDataRequest;
import com.weather.weatherinfoservice.models.WeatherDataResponse;
import com.weather.weatherinfoservice.services.WeatherServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherServiceImpl weatherServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldHealthCheckBeSuccessful() throws Exception {
        mockMvc.perform(get("/weather/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Weather service is running"));
    }

    @Test
    public void shouldGetWeatherDataSuccessfully() throws Exception {
        WeatherDataResponse mockData = new WeatherDataResponse(UUID.randomUUID(),"Auckland", "12", "C", "cloudy", LocalDate.now());

        Mockito.when(weatherServiceImpl.getWeatherData(mockData.getCity())).thenReturn(mockData);

        mockMvc.perform(get("/weather?city=" + mockData.getCity()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value(mockData.getCity()))
                .andExpect(jsonPath("$.temp").value(mockData.getTemp()))
                .andExpect(jsonPath("$.unit").value(mockData.getUnit()))
                .andExpect(jsonPath("$.weather").value(mockData.getWeather()));
    }

    @Test
    public void shouldThrowErrorWhenGetWeatherDataForNonExistingCity() throws Exception {

        String city = "Berlin";
        String errorMessage = "City not found";

        Mockito.doThrow(new CityNotFoundException(errorMessage)).when(weatherServiceImpl).getWeatherData(city);

        mockMvc.perform(get("/weather?city=" + city))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    public void shouldAddWeatherDataSuccessfully() throws Exception {
        WeatherDataResponse mockData = new WeatherDataResponse(UUID.randomUUID(), "Hamilton", "16", "C", "rainy", LocalDate.now());

        Mockito.when(weatherServiceImpl.addWeatherData(any(WeatherDataRequest.class))).thenReturn(mockData);

        mockMvc.perform(post("/weather")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockData)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    public void shouldThrowErrorWhenAddWeatherDataForExistingCity() throws Exception {

        WeatherDataRequest mockData = new WeatherDataRequest("Auckland", "16", "C", LocalDate.now(), "rainy");
        String errorMessage = "City already exist";

        Mockito.doThrow(new CityAlreadyExistException(errorMessage)).when(weatherServiceImpl).addWeatherData(mockData);

        mockMvc.perform(post("/weather")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockData)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    public void shouldUpdateWeatherDataSuccessfully() throws Exception {

        WeatherDataResponse mockData = new WeatherDataResponse(UUID.randomUUID(), "Wellington", "16", "C", "rainy", LocalDate.now());

        Mockito.when(weatherServiceImpl.updateWeatherData(any(WeatherDataRequest.class))).thenReturn(mockData);

        mockMvc.perform(put("/weather")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value(mockData.getCity()))
                .andExpect(jsonPath("$.temp").value(mockData.getTemp()))
                .andExpect(jsonPath("$.unit").value(mockData.getUnit()))
                .andExpect(jsonPath("$.weather").value(mockData.getWeather()));
    }

    @Test
    public void shouldThrowErrorWhenUpdateWeatherDataForNonExistingCity() throws Exception {

        WeatherDataRequest mockData = new WeatherDataRequest("Queenstown", "16", "C", LocalDate.now(), "rainy");
        String errorMessage = "City not found in the memory list";

        Mockito.doThrow(new CityNotFoundException(errorMessage)).when(weatherServiceImpl).updateWeatherData(mockData);

        mockMvc.perform(put("/weather")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockData)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    public void shouldDeleteWeatherDataSuccessfully() throws Exception {

        String city = "Auckland";

        Mockito.doNothing().when(weatherServiceImpl).deleteWeatherData(city);
        mockMvc.perform(delete("/weather?city=" + city))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldThrowErrorWhenDeleteWeatherDataForNonExistingCity() throws Exception {
        String city = "Berlin";
        String errorMessage = "City not found";

        Mockito.doThrow(new CityNotFoundException(errorMessage)).when(weatherServiceImpl).deleteWeatherData(city);
        mockMvc.perform(delete("/weather?city=" + city))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(errorMessage));
    }
}
