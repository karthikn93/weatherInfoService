package com.weather.weatherinfoservice.integration;

import com.weather.weatherinfoservice.models.WeatherDataRequest;
import com.weather.weatherinfoservice.models.WeatherDataResponse;
import com.weather.weatherinfoservice.repositories.WeatherDataEntity;
import com.weather.weatherinfoservice.repositories.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherAppTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WeatherRepository weatherRepository;

    // Get weather data for existing city - Positive
    @ParameterizedTest
    @ValueSource(strings = {"Auckland", "Nelson"})
    public void shouldGetWeatherDataForExistingCity(String city){
        ResponseEntity<WeatherDataResponse> response = restTemplate.getForEntity("/weather?city=" + city, WeatherDataResponse.class);

        assert(response.getStatusCode().value() == 200);
        assert(response.getBody() != null && response.getBody().getCity().equals(city));
        assert(response.getBody().getTemp() != null);
        assert(response.getBody().getUnit() != null);
        assert(response.getBody().getDate() != null);
        assert(response.getBody().getWeather() != null);
    }

    @Test
    public void shouldThrowErrorWhenCityNotFoundInAllSources(){
        String city = "New York";
        ResponseEntity<Map> response = restTemplate.getForEntity("/weather?city=" + city, Map.class);

        assert(response.getStatusCode().value() == 404);
        assert(response.getBody() != null && response.getBody().get("message").toString().equals(city + " data not found in all the sources"));
        assert(response.getBody().get("timestamp") != null);
    }

    // Add new weather data for new city - Positive
    @Test
    public void shouldAddWeatherDataSuccessfully(){
        WeatherDataRequest createData = new WeatherDataRequest("Hamilton", "17", "C", LocalDate.now(), "sunny");

        ResponseEntity<Map> response = restTemplate.postForEntity("/weather", createData, Map.class);

        assert(response.getStatusCode().value() == 201);
        assert(response.getBody().get("success").equals(true));
    }

    // Add new weather data for existing city - Negative
    @Test
    public void shouldThrowErrorWhenAddingWeatherDataForExistingCity(){
        WeatherDataRequest createdData = new WeatherDataRequest("Auckland", "17", "C", LocalDate.now(), "sunny");

        ResponseEntity<Map> response = restTemplate.postForEntity("/weather", createdData, Map.class);

        assert(response.getStatusCode().value() == 409);
        assert(response.getBody() != null && response.getBody().get("message").toString().equals(createdData.getCity() + " city already exist in memory, try to add it for new city"));
        assert(response.getBody().get("timestamp") != null);
    }

    // Update weather data for existing city - Positive
    @Test
    public void shouldUpdateWeatherDataSuccessfully(){
        WeatherDataRequest updatedDate = new WeatherDataRequest("Christchurch", "10", "C", LocalDate.now(), "rainy");
        ResponseEntity<WeatherDataRequest> response = restTemplate.exchange("/weather", HttpMethod.PUT, new HttpEntity<>(updatedDate), WeatherDataRequest.class);
        assert(response.getStatusCode().value() == 200);
        assert(response.getBody() != null && response.getBody().getCity().equals(updatedDate.getCity()));
        assert(response.getBody().getTemp().equals(updatedDate.getTemp()));
        assert(response.getBody().getUnit().equals(updatedDate.getUnit()));
        assert(response.getBody().getDate().equals(updatedDate.getDate()));
        assert(response.getBody().getWeather().equals(updatedDate.getWeather()));

        Optional<WeatherDataEntity> retrieveUpdatedData = weatherRepository.findWeatherByCity(updatedDate.getCity());
        assert(retrieveUpdatedData.get().getTemp().equals(updatedDate.getTemp()));
        assert(retrieveUpdatedData.get().getUnit().equals(updatedDate.getUnit()));
        assert(retrieveUpdatedData.get().getDate().equals(updatedDate.getDate()));
        assert(retrieveUpdatedData.get().getWeather().equals(updatedDate.getWeather()));
    }

    // Update weather data for non-existing city - Negative
    @Test
    public void shouldThrowErrorWhenUpdatingWeatherDataForNonExistingCity(){
        WeatherDataRequest updatedData = new WeatherDataRequest("Queenstown", "17", "C", LocalDate.now(), "sunny");

        ResponseEntity<Map> response = restTemplate.exchange("/weather", HttpMethod.PUT, new HttpEntity<>(updatedData), Map.class);

        assert(response.getStatusCode().value() == 404);
        assert(response.getBody() != null && response.getBody().get("message").toString().equals(updatedData.getCity() + " city not found in memory, try a city already in memory"));
        assert(response.getBody().get("timestamp") != null);
    }

    // Delete weather data for existing city - Positive
    @Test
    public void shouldDeleteWeatherDataSuccessfully(){
        String city = "Wellington";
        ResponseEntity<Void> response = restTemplate.exchange("/weather?city=" + city, HttpMethod.DELETE, null, Void.class);
        assert(response.getStatusCode().value() == 204);

        assert(weatherRepository.findWeatherByCity(city).isEmpty());
    }

    // Delete weather data for non-existing city - Negative
    @Test
    public void shouldThrowErrorWhenDeletingWeatherDataForNonExistingCity(){
        String city = "Sydney";
        ResponseEntity<Map> response = restTemplate.exchange("/weather?city=" + city, HttpMethod.DELETE, null, Map.class);
        assert(response.getStatusCode().value() == 404);
        assert(response.getBody() != null && response.getBody().get("message").toString().equals(city + " city not found in memory, try a city already in memory"));
        assert(response.getBody().get("timestamp") != null);
    }

}
