package pt.ul.fc.css.weatherwise.presentation;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    private static final String BASE_URL = "https://api.open-meteo.com/v1/forecast";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Map<String, Object> getForecast(double lat, double lon, String dateStr) {
        LocalDate requestedDate = LocalDate.parse(dateStr, FORMATTER);

        String url = String.format(
                "%s?latitude=%f&longitude=%f&daily=temperature_2m_max,temperature_2m_min,weathercode&timezone=UTC",
                BASE_URL, lat, lon
        );

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || !response.containsKey("daily")) {
            throw new RuntimeException("Weather data unavailable");
        }

        Map<String, Object> daily = (Map<String, Object>) response.get("daily");
        List<String> dates = (List<String>) daily.get("time");

        int index = dates.indexOf(requestedDate.toString());
        if (index == -1) {
            throw new RuntimeException("Forecast not available for the requested date");
        }

        int weatherCode = ((List<Number>) daily.get("weathercode")).get(index).intValue();
        String weatherDescription = mapWeatherCode(weatherCode);

        return Map.of(
                "date", requestedDate.format(FORMATTER),
                "temperature_max", ((List<Number>) daily.get("temperature_2m_max")).get(index),
                "temperature_min", ((List<Number>) daily.get("temperature_2m_min")).get(index),
                "weather", weatherDescription
        );
    }

    private static String mapWeatherCode(int code) {
        return switch (code) {
            case 0 -> "Clear sky";
            case 1 -> "Mainly clear";
            case 2 -> "Partly cloudy";
            case 3 -> "Overcast";
            case 45 -> "Fog";
            case 48 -> "Depositing rime fog";
            case 51 -> "Light drizzle";
            case 53 -> "Moderate drizzle";
            case 55 -> "Dense drizzle";
            case 56 -> "Light freezing drizzle";
            case 57 -> "Dense freezing drizzle";
            case 61 -> "Slight rain";
            case 63 -> "Moderate rain";
            case 65 -> "Heavy rain";
            case 66 -> "Light freezing rain";
            case 67 -> "Heavy freezing rain";
            case 71 -> "Slight snow fall";
            case 73 -> "Moderate snow fall";
            case 75 -> "Heavy snow fall";
            case 77 -> "Snow grains";
            case 80 -> "Slight rain showers";
            case 81 -> "Moderate rain showers";
            case 82 -> "Violent rain showers";
            case 85 -> "Slight snow showers";
            case 86 -> "Heavy snow showers";
            case 95 -> "Slight or moderate thunderstorm";
            case 96 -> "Thunderstorm with slight hail";
            case 99 -> "Thunderstorm with heavy hail";
            default -> "Unknown";
        };
    }
}
