package weather.app.weather_app.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import weather.app.weather_app.dao.CityDAO;
import weather.app.weather_app.dto.WeatherResponse;
import weather.app.weather_app.models.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void getCitiesWeatherList_shouldReturnEmptyListWhenNoCities() {
        // given
        User user = new User("login", "password");
        when(cityDAO.getCitiesCoordinates(user)).thenReturn(List.of());

        // when
        List<WeatherResponse> result = cityService.getCitiesWeatherList(user);

        // then
        assertTrue(result.isEmpty());
        verify(cityDAO).getCitiesCoordinates(user);
        verifyNoInteractions(restTemplate);
    }
}
