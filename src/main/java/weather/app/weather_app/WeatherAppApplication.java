package weather.app.weather_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import weather.app.weather_app.config.OpenWeatherConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(OpenWeatherConfigurationProperties.class)
public class WeatherAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherAppApplication.class, args);
    }
}
