package pt.ul.fc.css.javafx.api;

import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import pt.ul.fc.css.javafx.dto.BikeDTO;
import pt.ul.fc.css.javafx.dto.EndTripDTO;
import pt.ul.fc.css.javafx.dto.StationDTO;
import pt.ul.fc.css.javafx.dto.TripModDTO;
import pt.ul.fc.css.javafx.dto.TripReservedDTO;
import pt.ul.fc.css.javafx.dto.UserDetailsDTO;
import pt.ul.fc.css.javafx.dto.WeatherConditionDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080";

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private static final HttpClient client = HttpClient.newHttpClient();

    static {
        // Regista módulo para suportar LocalDateTime
        mapper.registerModule(new JavaTimeModule());
    }
    // GET ALL BIKES
    public static List<BikeDTO> getAllBicycles(String state) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/bikes?state=" + state))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to get bicycles: " + response.statusCode());
        }

        return mapper.readValue(
                response.body(),
                new TypeReference<List<BikeDTO>>() {}
        );
    }

	    // GET RESERVED TRIPS BY USER AND STATE
	    public static List<TripReservedDTO> getTrips(Long userId, String state) throws Exception {
	
	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(BASE_URL + "/trips/" + state + "/" + userId))
	                .GET()
	                .build();
	
	        HttpResponse<String> response =
	                client.send(request, HttpResponse.BodyHandlers.ofString());
	
	        if (response.statusCode() != 200) {
	            throw new RuntimeException(
	                    "Failed to get reserved trips: " +
	                            response.statusCode() + "\n" + response.body()
	            );
	        }
	
	        return mapper.readValue(
	                response.body(),
	                new TypeReference<List<TripReservedDTO>>() {}
	        );
	    }

       // GET Active TRIPS (by user)
       public static List<TripReservedDTO> getActiveTrips(Long userId) throws Exception {

           HttpRequest request = HttpRequest.newBuilder()
                   .uri(URI.create(BASE_URL + "/trips?" + userId))
                   .GET()
                   .build();

           HttpResponse<String> response =
                   client.send(request, HttpResponse.BodyHandlers.ofString());

           if (response.statusCode() != 200) {
               throw new RuntimeException(
                   "Failed to get reserved trips: " +
                   response.statusCode() + "\n" + response.body()
               );
           }

           return mapper.readValue(
                   response.body(),
                   new TypeReference<List<TripReservedDTO>>() {}
           );
       }


    
 // CREATE RESERVATION
    public static void reserveTrip(Long userId, Long bikeId, String date) throws Exception {
        TripModDTO tripRequest = new TripModDTO(bikeId, userId);
        String json = mapper.writeValueAsString(tripRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/trips/reserve/" + date))  // send date in path
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Failed to reserve trip: " + response.statusCode() + "\n" + response.body());
        }
    }

 // CANCEL RESERVATION (by tripId)
    public static void cancelTrip(Long tripId) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/trips/cancel/" + tripId))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                "Failed to cancel trip: " + response.statusCode() + "\n" + response.body()
            );
        }
    }







    
    public static boolean testConnection() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/bikes")) // or any endpoint
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


	public static WeatherConditionDTO getWeatherForStation(Long id, String dateStr) {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<StationDTO> getAllStations(String date) throws Exception {
	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(BASE_URL + "/stations?date=" + date)) // optional query param
	            .GET()
	            .build();

	    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

	    if (response.statusCode() != 200) {
	        throw new RuntimeException("Failed to get stations: " + response.statusCode() + "\n" + response.body());
	    }

	    return mapper.readValue(response.body(), new TypeReference<List<StationDTO>>() {});
	}
	  //GET ALL STATIONS
    public static List<StationDTO> getAllStations() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/stations"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to load stations: " + response.statusCode());
        }

        return mapper.readValue(
            response.body(),
            new TypeReference<List<StationDTO>>() {}
        );
    }

    //GET STATION DETAILS
    public static StationDTO getStationDetails(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/stations/" + id))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to load station details: " + response.statusCode());
        }
        
        return mapper.readValue(response.body(), StationDTO.class);
    }

	public static void endTrip(Long tripId, EndTripDTO req) throws Exception {

	    String json = mapper.writeValueAsString(req);

	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(BASE_URL + "/trips/" + tripId + "/end"))
	            .header("Content-Type", "application/json")
	            .POST(HttpRequest.BodyPublishers.ofString(json))
	            .build();

	    HttpResponse<String> response =
	            client.send(request, HttpResponse.BodyHandlers.ofString());

	    if (response.statusCode() != 200) {
	        throw new RuntimeException(
	                "Failed to end trip: " +
	                response.statusCode() + "\n" + response.body()
	        );
	    }

	}


	public static void startTrip(TripModDTO req) throws Exception {

	    String json = mapper.writeValueAsString(req);

	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(BASE_URL + "/trips/start"))
	            .header("Content-Type", "application/json")
	            .POST(HttpRequest.BodyPublishers.ofString(json))
	            .build();

	    HttpResponse<String> response =
	            client.send(request, HttpResponse.BodyHandlers.ofString());

	    if (response.statusCode() != 200) {
	        throw new RuntimeException(
	                "Failed to start trip: " +
	                response.statusCode() + "\n" + response.body()
	        );
	    }

	}


	 // --- LOGIN ---
	    public static Long login(String email, String password) throws Exception {

	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(
	                        BASE_URL + "/users/login?email=" + email + "&password=" + password
	                ))
	                .POST(HttpRequest.BodyPublishers.noBody())
	                .build();
	        System.out.println(request);
	        HttpResponse<String> response =
	                client.send(request, HttpResponse.BodyHandlers.ofString());

	        if (response.statusCode() == 404) {
	            throw new Exception("User not found");
	        }

	        if (response.statusCode() == 401 || response.statusCode() == 403) {
	            throw new Exception("Invalid password");
	        }

	        if (response.statusCode() != 200) {
	            throw new RuntimeException("Login failed: " + response.statusCode());
	        }

	        // Aqui mock: token = ID do user
	        return Long.parseLong(response.body());
	    }



	    // --- GET USER DETAILS ---
	    public static UserDetailsDTO getUserDetails(Long userId) throws Exception {


	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(BASE_URL + "/users/" + userId))
	                .GET()
	                .build();

	        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

	        if (response.statusCode() != 200) {
	            throw new RuntimeException("Failed to get user details: " + response.statusCode() + "\n" + response.body());
	        }

	        return mapper.readValue(response.body(), UserDetailsDTO.class);
	    }


    

}