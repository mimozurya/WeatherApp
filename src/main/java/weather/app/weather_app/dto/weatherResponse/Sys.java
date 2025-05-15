package weather.app.weather_app.dto.weatherResponse;

public record Sys(
        Integer type,
        Integer id,
        String country,
        Long sunrise,
        Long sunset
) {
}
