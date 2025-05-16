package weather.app.weather_app.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import weather.app.weather_app.dto.UserResponse;
import weather.app.weather_app.dto.WeatherResponse;
import weather.app.weather_app.models.Location;
import weather.app.weather_app.models.User;
import weather.app.weather_app.services.CityService;
import weather.app.weather_app.services.SessionService;
import weather.app.weather_app.services.UserService;

import java.util.List;
import java.util.UUID;

import static weather.app.weather_app.utils.AuthUtils.COOKIE_NAME;
import static weather.app.weather_app.utils.AuthUtils.SIGN_IN_URL;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;
    private final CityService cityService;

    @GetMapping("/sign-up")
    public String signUp(@CookieValue(value = COOKIE_NAME, required = false) UUID sessionId, Model model) {
        if (sessionId != null) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        return "registration/sign-up";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute @Valid UserResponse userResponse, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/sign-up-with-errors";
        }
        return userService.registerNewUser(userResponse);
    }

    @GetMapping("/sign-in")
    public String signIn(@CookieValue(value = COOKIE_NAME, required = false) UUID sessionId, Model model) {
        if (sessionId != null) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        return "login/sign-in";
    }

    @PostMapping("/authorization")
    public String authorizeUser(@ModelAttribute @Valid UserResponse userResponse, HttpServletResponse response) {
        User foundUser = userService.authorizeUser(userResponse);
        if (foundUser == null) {
            return SIGN_IN_URL;
        }

        response.addCookie(sessionService.createSessionCookie(foundUser));
        return "redirect:/";
    }

    @GetMapping("/")
    public String index(@CookieValue(value = COOKIE_NAME, required = false) UUID sessionId, Model model) {
        User currentUser = sessionService.getUserBySessionId(sessionId);
        if (sessionId == null && currentUser == null) {
            return SIGN_IN_URL;
        }

        List<WeatherResponse> weatherList = cityService.getCitiesWeatherList(currentUser);
        model.addAttribute("user", currentUser);
        model.addAttribute("weatherList", weatherList);

        return "index";
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(COOKIE_NAME) UUID sessionId, HttpServletResponse response) {
        sessionService.deleteSession(sessionId, response);
        return SIGN_IN_URL;
    }

    @DeleteMapping("/city")
    public String deleteCity(@CookieValue(value = COOKIE_NAME, required = false) UUID sessionId,
                             @ModelAttribute Location location) {
        User currentUser = sessionService.getUserBySessionId(sessionId);
        if (sessionId == null && currentUser == null) {
            return SIGN_IN_URL;
        }

        cityService.deleteCity(currentUser, location);

        return "redirect:/";
    }

    @GetMapping("/sign-up-with-errors")
    public String signUpWithError() {
        return "registration/sign-up-with-errors";
    }
}
