package pt.ul.fc.css.weatherwise.presentation.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.weatherwise.presentation.WeatherService;

@RestController
@RequestMapping("/weather")
@Tag(name = "Weather Controller", description = "Provides weather forecasts")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/forecast")
    @Operation(summary = "Get forecast for given latitude, longitude, and date (DD-MM-YYYY)")
    public ResponseEntity<?> getForecast(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam String date
    ) {
        try {
            return ResponseEntity.ok(weatherService.getForecast(lat, lon, date));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
