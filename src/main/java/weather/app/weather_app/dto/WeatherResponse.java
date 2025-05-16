package weather.app.weather_app.dto;

import weather.app.weather_app.dto.weatherResponse.*;

import java.util.List;

public record WeatherResponse(
        Coord coord,
        List<Weather> weather,
        String base,
        Main main,
        String visibility,
        Wind wind,
        Clouds clouds,
        Long dt,
        Sys sys,
        String timezone,
        Long id,
        String name,
        Integer cod
) {}
