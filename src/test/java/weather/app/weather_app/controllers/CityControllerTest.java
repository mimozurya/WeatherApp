package weather.app.weather_app.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import weather.app.weather_app.dto.CityResponse;
import weather.app.weather_app.models.User;
import weather.app.weather_app.services.CityService;
import weather.app.weather_app.services.SessionService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityControllerTest {

    @Mock
    private CityService cityService;

    @Mock
    private SessionService sessionService;

    @Mock
    private Model model;

    @InjectMocks
    private CityController cityController;

    @Test
    void searchCities_shouldReturnSearchResults_whenUserAuthenticated() {
        // given
        UUID sessionId = UUID.randomUUID();
        User user = new User("test", "pass");
        List<CityResponse> cities = List.of(
                new CityResponse("Moscow",  55.75, 37.61, "RU", "MoscowState"),
                new CityResponse("London",  51.50, -0.12, "UK", "LondonState")
        );

        when(sessionService.getUserBySessionId(sessionId)).thenReturn(user);
        when(cityService.getCityList("Moscow")).thenReturn(cities);

        // when
        String viewName = cityController.searchCities("Moscow", sessionId, model);

        // then
        assertEquals("search-results", viewName);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("cityList", cities);
    }

    @Test
    void addLocationCity_shouldSaveCityAndRedirect_whenUserAuthenticated() {
        // given
        UUID sessionId = UUID.randomUUID();
        User user = new User("test", "pass");
        CityResponse city = new CityResponse("Moscow",  55.75, 37.61, "RU", "MoscowState");

        when(sessionService.getUserBySessionId(sessionId)).thenReturn(user);

        // when
        String viewName = cityController.addLocationCity(city, sessionId);

        // then
        assertEquals("redirect:/", viewName);
        verify(cityService).saveCity(user, city);
    }

    @Test
    void addLocationCity_shouldRedirectToSignIn_whenSessionInvalid() {
        // given
        UUID invalidSessionId = UUID.randomUUID();
        CityResponse city = new CityResponse("Moscow",  55.75, 37.61, "RU", "MoscowState");

        when(sessionService.getUserBySessionId(invalidSessionId)).thenReturn(null);

        // when
        String viewName = cityController.addLocationCity(city, invalidSessionId);

        // then
        assertEquals("redirect:/", viewName);
        verifyNoInteractions(cityService);
    }
}
