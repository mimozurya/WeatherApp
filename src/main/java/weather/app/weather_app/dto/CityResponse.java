package weather.app.weather_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CityResponse {
    private String name;
    private double lat;
    private double lon;
    private String country;
    private String state;
}
