package weather.app.weather_app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import weather.app.weather_app.dto.CityResponse;
import weather.app.weather_app.models.User;
import weather.app.weather_app.services.CityService;
import weather.app.weather_app.services.SessionService;

import java.util.List;
import java.util.UUID;

@Controller
public class CityController {
    private final SessionService sessionService;
    private final CityService cityService;

    public CityController(SessionService sessionService, CityService cityService) {
        this.sessionService = sessionService;
        this.cityService = cityService;
    }

    @GetMapping("/search-results")
    public String searchCities(@RequestParam String city,
                               @CookieValue(value = UserController.COOKIE_NAME, required = false) UUID sessionId,
                               Model model) {
        if (sessionId == null) return "redirect:/sign-in";
        User currentUser = sessionService.getUserBySessionId(sessionId);
        if (currentUser == null) return "redirect:/sign-in";

        List<CityResponse> cityList = cityService.getCityList(city);

        model.addAttribute("user", currentUser);
        model.addAttribute("cityList", cityList);

        return "search-results";
    }

    @PostMapping("/add-location")
    public String addLocationCity(@ModelAttribute("city") CityResponse city,
                                  @CookieValue(value = UserController.COOKIE_NAME, required = false) UUID sessionId) {
        if (sessionId == null) return "redirect:/sign-in";
        User currentUser = sessionService.getUserBySessionId(sessionId);
        if (currentUser == null) return "redirect:/sign-in";

        cityService.saveCity(currentUser, city);

        return "redirect:/";
    }
}
