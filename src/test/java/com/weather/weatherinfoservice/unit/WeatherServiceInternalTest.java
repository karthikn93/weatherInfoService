package com.weather.weatherinfoservice.unit;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.weather.weatherinfoservice.exceptions.CityAlreadyExistException;
import com.weather.weatherinfoservice.exceptions.CityNotFoundException;
import com.weather.weatherinfoservice.models.WeatherDataRequest;
import com.weather.weatherinfoservice.models.WeatherDataResponse;
import com.weather.weatherinfoservice.repositories.WeatherDataEntity;
import com.weather.weatherinfoservice.repositories.WeatherRepository;
import com.weather.weatherinfoservice.services.WeatherServiceExternal;
import com.weather.weatherinfoservice.services.WeatherServiceInternal;
import com.weather.weatherinfoservice.services.WeatherServiceReader;
import com.weather.weatherinfoservice.util.IdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceInternalTest {

    @Mock
    private WeatherRepository weatherRepository;

    @Spy
    private WeatherServiceExternal weatherServiceExternal = new WeatherServiceExternal();

    @Spy
    private IdGenerator idGenerator = new IdGenerator();

    @InjectMocks
    private WeatherServiceInternal WeatherServiceInternal;

    @ParameterizedTest
    @ValueSource(strings = {"Auckland", "Hamilton"})
    public void shouldGetWeatherDataForExistingCity(String city){
        // Arrange
        WeatherDataEntity mockData = new WeatherDataEntity(UUID.randomUUID(), city, "11", "C", "sunny", LocalDate.now());
        if (city.equals("Auckland")) {
            when(weatherRepository.findWeatherByCity(mockData.getCity())).thenReturn(Optional.of(mockData));
        }
        else {
            when(weatherRepository.findWeatherByCity(mockData.getCity())).thenReturn(Optional.empty());
        }

        // Act
        WeatherDataResponse weatherDataResponse = WeatherServiceInternal.getWeatherData(mockData.getCity());

        // Assert
        assertThat(weatherDataResponse).isNotNull();
        assertThat(weatherDataResponse.getCity()).isEqualTo(mockData.getCity());
        assertThat(weatherDataResponse.getWeather()).isEqualTo(mockData.getWeather());
        assertThat(weatherDataResponse.getTemp()).isEqualTo(mockData.getTemp());
        assertThat(weatherDataResponse.getUnit()).isEqualTo(mockData.getUnit());
        verify(weatherRepository).findWeatherByCity(mockData.getCity());
    }

    @Test
    public void shouldThrowErrorWhenCityNotFoundInAllSources(){
        // Arrange
        String city = "New York";
        when(weatherRepository.findWeatherByCity(city)).thenReturn(Optional.empty());

        // Assert
        assertThrows(CityNotFoundException.class, () -> WeatherServiceInternal.getWeatherData(city));
        verify(weatherRepository).findWeatherByCity(city);
    }

    @Test
    public void shouldThrowErrorForNonExistingCity(){
        // Arrange
        String city = "Auckland";
        doThrow(new CityNotFoundException(city)).when(weatherRepository).findWeatherByCity(city);

        // Assert
        assertThrows(CityNotFoundException.class, () -> WeatherServiceInternal.getWeatherData(city));
        verify(weatherRepository).findWeatherByCity("Auckland");
    }

    @Test
    public void shouldAddWeatherDataToRepository(){
        // Arrange
        UUID id = UUID.randomUUID();
        WeatherDataEntity mockData = new WeatherDataEntity(id, "Hamilton", "11", "C", "sunny", LocalDate.now());
        when(idGenerator.generateId()).thenReturn(id);
        when(weatherRepository.saveWeather(mockData.getCity(), mockData)).thenReturn(mockData);

        // Act
        WeatherDataRequest request = new WeatherDataRequest("Hamilton", "11", "C", LocalDate.now(), "sunny");
        WeatherDataResponse weatherDataEntity = WeatherServiceInternal.addWeatherData(request);

        // Assert
        assertThat(weatherDataEntity).isNotNull();
        assertThat(weatherDataEntity.getCity()).isEqualTo(mockData.getCity());
        assertThat(weatherDataEntity.getWeather()).isEqualTo(mockData.getWeather());
        assertThat(weatherDataEntity.getDate()).isEqualTo(mockData.getDate());
        assertThat(weatherDataEntity.getTemp()).isEqualTo(mockData.getTemp());
        assertThat(weatherDataEntity.getUnit()).isEqualTo(mockData.getUnit());
        verify(weatherRepository).saveWeather(mockData.getCity(), mockData);
    }

    @Test
    public void shouldThrowErrorWhenAddExistingWeatherDataToRepository(){
        // Arrange
        UUID id = UUID.randomUUID();
        WeatherDataRequest request = new WeatherDataRequest("Hamilton", "11", "C", LocalDate.now(), "sunny");
        WeatherDataEntity mockData = new WeatherDataEntity(id, "Hamilton", "11", "C", "sunny", LocalDate.now());
        when(idGenerator.generateId()).thenReturn(id);
        doThrow(new CityAlreadyExistException("city already exist")).when(weatherRepository).saveWeather(mockData.getCity(), mockData);

        // Assert
        assertThrows(CityAlreadyExistException.class,() -> WeatherServiceInternal.addWeatherData(request));
        verify(weatherRepository).saveWeather(mockData.getCity(), mockData);
    }

    @Test
    public void shouldUpdateWeatherDataToRepository(){
        // Arrange

        UUID id = UUID.randomUUID();
        WeatherDataRequest request = new WeatherDataRequest("Auckland", "5", "C", LocalDate.now(), "cloudy");
        WeatherDataEntity mockData = new WeatherDataEntity(id, "Auckland", "5", "C", "cloudy", LocalDate.now());
        when(idGenerator.generateId()).thenReturn(id);
        when(weatherRepository.findWeatherByCity(mockData.getCity())).thenReturn(Optional.of(mockData));
        when(weatherRepository.updateWeather(mockData.getCity(), mockData)).thenReturn(mockData);

        // Act
        WeatherDataResponse weatherDataRequest = WeatherServiceInternal.updateWeatherData(request);

        // Assert
        assertThat(weatherDataRequest).isNotNull();
        assertThat(weatherDataRequest.getCity()).isEqualTo(mockData.getCity());
        assertThat(weatherDataRequest.getWeather()).isEqualTo(mockData.getWeather());
        assertThat(weatherDataRequest.getDate()).isEqualTo(mockData.getDate());
        assertThat(weatherDataRequest.getTemp()).isEqualTo(mockData.getTemp());
        assertThat(weatherDataRequest.getUnit()).isEqualTo(mockData.getUnit());
        verify(weatherRepository).updateWeather(mockData.getCity(), mockData);
    }

    @Test
    public void shouldThrowErrorWhenUpdateNonExistingWeatherDataToRepository(){
        // Arrange
        WeatherDataRequest mockData = new WeatherDataRequest("Queenstown", "11", "C", LocalDate.now(), "sunny");
        when(weatherRepository.findWeatherByCity(mockData.getCity())).thenReturn(Optional.empty());

        // Assert
        assertThrows(CityNotFoundException.class, () -> WeatherServiceInternal.updateWeatherData(mockData) );
        verify(weatherRepository).findWeatherByCity(mockData.getCity());

    }

    @Test
    public void shouldDeleteWeatherDataFromRepository(){
        // Arrange
        WeatherDataRequest request = new WeatherDataRequest("Auckland", "11", "C", LocalDate.now(), "sunny");
        WeatherDataEntity mockData = new WeatherDataEntity(UUID.randomUUID(), "Auckland", "11", "C", "sunny", LocalDate.now());
        when(weatherRepository.findWeatherByCity(mockData.getCity())).thenReturn(Optional.of(mockData));
        doNothing().when(weatherRepository).deleteWeather(mockData.getCity());

        // Act
        WeatherServiceInternal.deleteWeatherData(request.getCity());

        // Assert;
        verify(weatherRepository).deleteWeather(mockData.getCity());
    }

    @Test
    public void shouldThrowErrorWhenDeleteNonExistingWeatherDataFromRepository(){
        String city = "Auckland";
        when(weatherRepository.findWeatherByCity(city)).thenReturn(Optional.empty());

        assertThrows(CityNotFoundException.class, () -> WeatherServiceInternal.deleteWeatherData(city));
        verify(weatherRepository).findWeatherByCity(city);
    }
}
