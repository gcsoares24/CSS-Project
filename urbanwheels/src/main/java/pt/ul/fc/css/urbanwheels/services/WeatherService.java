package pt.ul.fc.css.urbanwheels.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pt.ul.fc.css.urbanwheels.dto.WeatherConditionDTO;
import pt.ul.fc.css.urbanwheels.entities.Station;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class WeatherService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Value("${weatherwise.base-url:http://host.docker.internal:8081/weather/forecast}")
    private String weatherwiseBaseUrl;

    public WeatherConditionDTO getCurrentWeather(double lat, double lon, String date) {
        // Validate coordinates first
        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            System.err.println("Invalid coordinates: lat=" + lat + ", lon=" + lon);
            return new WeatherConditionDTO("Invalid", null, null, null);
        }

        String today = LocalDate.now().format(FORMATTER);

        String url = UriComponentsBuilder
                .fromHttpUrl(weatherwiseBaseUrl)
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("date", date)
                .toUriString();

        System.out.println("Requesting weather from: " + url);

        try {
            WeatherConditionDTO weather = restTemplate.getForObject(url, WeatherConditionDTO.class);
            if (weather == null) {
                // Return default if WeatherWise responds but body is empty
                return new WeatherConditionDTO("Unknown", null, null, null);
            }
            return weather;
        } catch (RestClientException e) {
            // Log the error and return default instead of null
            System.err.println("WeatherWise service unreachable: " + e.getMessage());
            return new WeatherConditionDTO("Unavailable", null, null, null);
        }
    }


    public List<WeatherConditionDTO> getWeatherForStations(List<Station> stations, String date) {
        if (stations == null || stations.isEmpty()) {
            return Collections.emptyList();
        }

        List<WeatherConditionDTO> result = new ArrayList<>();

        for (Station station : stations) {
            WeatherConditionDTO weather = getCurrentWeather(station.getLat(), station.getLon(), date);
            result.add(weather);
        }

        return result;
    }
}
