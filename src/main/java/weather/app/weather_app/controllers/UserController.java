package weather.app.weather_app.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import weather.app.weather_app.dto.WeatherResponse;
import weather.app.weather_app.models.Location;
import weather.app.weather_app.models.User;
import weather.app.weather_app.services.CityService;
import weather.app.weather_app.services.SessionService;
import weather.app.weather_app.services.UserService;

import java.util.List;
import java.util.UUID;

@Controller
public class UserController {
    public static final String COOKIE_NAME = "WEATHER_SESSION_ID";
    private final UserService userService;
    private final SessionService sessionService;
    private final CityService cityService;

    public UserController(UserService userService, SessionService sessionService, CityService cityService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.cityService = cityService;
    }

    @GetMapping("/sign-up")
    public String signUpPage(@CookieValue(value = COOKIE_NAME, required = false) UUID sessionId, Model model) {
        if (sessionId != null) return "redirect:/";

        model.addAttribute("user", new User());
        return "registration/sign-up";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "redirect:/sign-up-with-errors";
        return userService.registerNewUser(user);
    }

    @GetMapping("/sign-in")
    public String signInPage(@CookieValue(value = COOKIE_NAME, required = false) UUID sessionId, Model model) {
        if (sessionId != null) return "redirect:/";

        model.addAttribute("user", new User());
        return "login/sign-in";
    }

    @PostMapping("/authorization")
    public String authorizationUser(@ModelAttribute User user, HttpServletResponse response) {
        User foundUser = userService.authorizationUser(user);
        if (foundUser == null) return "redirect:/sign-in";

        response.addCookie(sessionService.createSessionCookie(foundUser));
        return "redirect:/";
    }

    @GetMapping("/")
    public String indexPage(@CookieValue(value = COOKIE_NAME, required = false) UUID sessionId, Model model) {
        if (sessionId == null) return "redirect:/sign-in";
        User currentUser = sessionService.getUserBySessionId(sessionId);
        if (currentUser == null) return "redirect:/sign-in";

        List<WeatherResponse> weatherList = cityService.getCitiesWeatherList(currentUser);
        model.addAttribute("user", currentUser);
        model.addAttribute("weatherList", weatherList);

        return "index";
    }

    @GetMapping("/logout")
    public String logoutPage(@CookieValue(COOKIE_NAME) UUID sessionId, HttpServletResponse response) {
        response.addCookie(sessionService.deleteSessionCookie());
        sessionService.deleteSession(sessionId);
        return "redirect:/sign-in";
    }

    @PostMapping("/delete-city")
    public String deleteCity(@CookieValue(value = COOKIE_NAME, required = false) UUID sessionId,
                             @ModelAttribute Location location) {
        if (sessionId == null) return "redirect:/sign-in";
        User currentUser = sessionService.getUserBySessionId(sessionId);
        if (currentUser == null) return "redirect:/sign-in";

        cityService.deleteCity(currentUser, location);

        return "redirect:/";
    }

    @GetMapping("/sign-up-with-errors")
    public String signUpWithErrorPage() {
        return "registration/sign-up-with-errors";
    }
}
