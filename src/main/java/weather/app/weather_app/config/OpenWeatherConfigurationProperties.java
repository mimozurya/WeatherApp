package weather.app.weather_app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openweathermap.api")
@Getter
@Setter
public class OpenWeatherConfigurationProperties {
    private String key;
    private String urlCity;
    private String urlWeather;
}
