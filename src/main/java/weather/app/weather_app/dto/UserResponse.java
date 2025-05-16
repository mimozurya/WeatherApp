package weather.app.weather_app.dto;

import jakarta.validation.constraints.Size;

public record UserResponse(
        @Size(min = 3) String login,
        @Size(min = 3) String password
) {}
