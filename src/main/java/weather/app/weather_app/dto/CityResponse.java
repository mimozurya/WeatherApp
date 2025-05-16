package weather.app.weather_app.dto;

public record CityResponse(String name,
                           double lat,
                           double lon,
                           String country,
                           String state) {}
