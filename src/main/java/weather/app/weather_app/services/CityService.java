package weather.app.weather_app.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import weather.app.weather_app.dao.CityDAO;
import weather.app.weather_app.dto.CityResponse;
import weather.app.weather_app.dto.WeatherResponse;
import weather.app.weather_app.models.Location;
import weather.app.weather_app.models.User;
import weather.app.weather_app.services.restclient.OpenWeatherRestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityDAO cityDAO;
    private final OpenWeatherRestClient weatherClient;

    public List<CityResponse> getCityList(String cityName) {
        return weatherClient.getCityList(cityName);
    }

    public void saveCity(User user, CityResponse city) {
        Location location = new Location(city.name(), city.lat(), city.lon());
        cityDAO.saveCityLocation(user, location);
    }

    public List<WeatherResponse> getCitiesWeatherList(User user) {
        return cityDAO.getCitiesCoordinates(user).stream()
                .map(location -> weatherClient.getWeather(location.getLatitude(), location.getLongitude())).toList();
    }

    public void deleteCity(User user, Location location) {
        cityDAO.deleteCity(user, location);
    }
}
