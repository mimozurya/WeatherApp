package weather.app.weather_app.dto.weatherResponse;

public record Weather(
        Integer id,
        String main,
        String description,
        String icon
) {
}
