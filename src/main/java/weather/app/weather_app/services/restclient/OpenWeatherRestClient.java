package weather.app.weather_app.services.restclient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import weather.app.weather_app.config.OpenWeatherConfigurationProperties;
import weather.app.weather_app.dto.CityResponse;
import weather.app.weather_app.dto.WeatherResponse;
import weather.app.weather_app.exceptions.ApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OpenWeatherRestClient {
    private final RestTemplate restTemplate;
    private final OpenWeatherConfigurationProperties weatherConfig;

    public WeatherResponse getWeather(double latitude, double longitude) {
        String weatherUrl = String.format("%s?lat=%f&lon=%f&appid=%s",
                weatherConfig.getUrlWeather(), latitude, longitude, weatherConfig.getKey()
        );
        try {
            ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(weatherUrl, WeatherResponse.class);
            return Objects.requireNonNull(response.getBody());
        } catch (Exception e) {
            throw new ApiException("Failed to get weather data from OpenWeatherAPI", e);
        }
    }

    public List<CityResponse> getCityList (String cityName) {
        String url = String.format("%s?q=%s&limit=5&appid=%s", weatherConfig.getUrlCity(), cityName, weatherConfig.getKey());
        try {
            ResponseEntity<CityResponse[]> response = restTemplate.getForEntity(url, CityResponse[].class);
            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            throw new ApiException("Failed to get city data from OpenWeatherAPI", e);
        }
    }
}
