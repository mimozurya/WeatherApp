package weather.app.weather_app.dto.weatherResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Main(
        Double temp,
        @JsonProperty("feels_like") Double feelsLike,
        @JsonProperty("temp_min") Double tempMin,
        @JsonProperty("temp_max") Double tempMax,
        Integer pressure,
        Integer humidity,
        @JsonProperty("sea_level") Integer seaLevel,
        @JsonProperty("grnd_level") Integer grndLevel
        ) {
}
