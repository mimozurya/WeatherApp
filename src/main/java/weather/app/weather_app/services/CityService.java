package weather.app.weather_app.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import weather.app.weather_app.dao.CityDAO;
import weather.app.weather_app.dto.CityResponse;
import weather.app.weather_app.dto.WeatherResponse;
import weather.app.weather_app.models.Location;
import weather.app.weather_app.models.User;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class CityService {
    private final CityDAO cityDAO;
    private final RestTemplate restTemplate;

    @Value("${openweathermap.api.key}")
    private String API_KEY;
    @Value("${openweathermap.api.url.city}")
    private String API_CITY_URL;
    @Value("${openweathermap.api.url.weather}")
    private String API_WEATHER_URL;

    public CityService(RestTemplate restTemplate, CityDAO cityDAO) {
        this.restTemplate = restTemplate;
        this.cityDAO = cityDAO;
    }

    public List<CityResponse> getCityList(String cityName) {
        String url = String.format("%s?q=%s&limit=5&appid=%s", API_CITY_URL, cityName, API_KEY);
        ResponseEntity<CityResponse[]> response = restTemplate.getForEntity(url, CityResponse[].class);
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    public void saveCity(User user, CityResponse city) {
        Location location = new Location(city.getName(), city.getLat(), city.getLon());
        cityDAO.saveCityLocation(user, location);
    }

    public List<WeatherResponse> getCitiesWeatherList(User user) {
        return cityDAO.getCitiesCoordinates(user).stream()
                .map(location -> {
                    String weatherUrl = String.format("%s?lat=%f&lon=%f&appid=%s",
                            API_WEATHER_URL, location.getLatitude(), location.getLongitude(), API_KEY
                    );
                    ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(weatherUrl, WeatherResponse.class);
                    return Objects.requireNonNull(response.getBody());
                }).toList();
    }

    public void deleteCity(User user, Location location) {
        cityDAO.deleteCityLocation(user, location);
    }
}
