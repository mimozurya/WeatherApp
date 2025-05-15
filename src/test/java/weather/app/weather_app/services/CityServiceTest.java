package weather.app.weather_app.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import weather.app.weather_app.dao.CityDAO;
import weather.app.weather_app.dto.CityResponse;
import weather.app.weather_app.dto.WeatherResponse;
import weather.app.weather_app.models.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CityDAO cityDAO;

    @InjectMocks
    private CityService cityService;

    @Test
    void getCityListTest_shouldReturnCities_whenApiReturnsData() {
        //given
        CityResponse[] mockCities = {
                new CityResponse("Moscow", 55.7558, 37.6176, "RU", "Moscow"),
                new CityResponse("Moscow", 46.7324, -117.0002, "US", "Canada")
        };
        when(restTemplate.getForEntity(anyString(), Mockito.eq(CityResponse[].class)))
                .thenReturn(new ResponseEntity<>(mockCities, HttpStatus.OK));

        //when
        List<CityResponse> result = cityService.getCityList("Moscow");

        //then
        assertEquals(2, result.size());
        assertEquals("Moscow", result.get(0).name());
        assertEquals("RU", result.get(0).country());
    }

    @Test
    void getCityListTest_shouldReturnEmptyList_whenCityNotFound() {
        //given
        when(restTemplate.getForEntity(anyString(), Mockito.eq(CityResponse[].class)))
                .thenReturn(new ResponseEntity<>(new CityResponse[0], HttpStatus.OK));

        //when
        List<CityResponse> result = cityService.getCityList("UnknownCity");

        //then
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    void getCitiesWeatherList_shouldReturnEmptyListWhenNoCities() {
        // given
        User user = new User(1, "login", "password");
        when(cityDAO.getCitiesCoordinates(user)).thenReturn(List.of());

        // when
        List<WeatherResponse> result = cityService.getCitiesWeatherList(user);

        // then
        assertTrue(result.isEmpty());
        verify(cityDAO).getCitiesCoordinates(user);
        verifyNoInteractions(restTemplate);
    }
}
