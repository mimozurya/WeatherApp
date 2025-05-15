package weather.app.weather_app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import weather.app.weather_app.dto.CityResponse;
import weather.app.weather_app.models.User;
import weather.app.weather_app.services.CityService;
import weather.app.weather_app.services.SessionService;

import java.util.List;
import java.util.UUID;

import static weather.app.weather_app.utils.AuthUtils.COOKIE_NAME;
import static weather.app.weather_app.utils.AuthUtils.SIGN_IN_URL;

@Controller
@RequiredArgsConstructor
public class CityController {
    private final SessionService sessionService;
    private final CityService cityService;

    @GetMapping("/search-results")
    public String searchCities(@RequestParam String city,
                               @CookieValue(value = COOKIE_NAME, required = false) UUID sessionId,
                               Model model) {
        User currentUser = sessionService.getUserBySessionId(sessionId);
        if (sessionId == null && currentUser == null) {
            return SIGN_IN_URL;
        }

        List<CityResponse> cityList = cityService.getCityList(city);

        model.addAttribute("user", currentUser);
        model.addAttribute("cityList", cityList);

        return "search-results";
    }

    @PostMapping("/add-location")
    public String addLocationCity(@ModelAttribute("city") CityResponse city,
                                  @CookieValue(value = COOKIE_NAME, required = false) UUID sessionId) {
        User currentUser = sessionService.getUserBySessionId(sessionId);
        if (sessionId == null && currentUser == null) {
            return SIGN_IN_URL;
        }

        cityService.saveCity(currentUser, city);

        return "redirect:/";
    }
}
