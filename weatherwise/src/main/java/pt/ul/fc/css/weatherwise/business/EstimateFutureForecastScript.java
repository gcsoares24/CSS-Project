package pt.ul.fc.css.weatherwise.business;

import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuditRecordGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuthorGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.LocationRowGateway; 
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.WeatherRecordGateway;
import pt.ul.fc.css.weatherwise.entities.Author;
import pt.ul.fc.css.weatherwise.entities.Location;
import pt.ul.fc.css.weatherwise.entities.WeatherRecord;
import pt.ul.fc.css.weatherwise.entities.WeatherCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class EstimateFutureForecastScript {

	/**
	 * Executes Use Case D: Calculates future forecast estimation for a given location.
	 * Retrieves historical weather data, calculates the most probable condition,
	 * and displays or returns the estimated forecast.
	 * @throws PersistenceException 
	*/
	public String execute(LocationRowGateway locationGateway, WeatherRecordGateway weatherRecord,AuditRecordGateway auditRecord, AuthorGateway authorGateway, String locationName, int numberOfDays, String authorName) throws ApplicationException, PersistenceException {
	   
	    List<WeatherRecord> historicalData = getHistoricalData(locationGateway, weatherRecord,  locationName, numberOfDays);

	    List<String> conditions = extractWeatherConditions(historicalData);

	    String estimatedCondition = calculateMode(conditions);

	    //add to the db the audit
	    Author author = authorGateway.findOrCreateByName(authorName);
        Location location = locationGateway.getByName(locationName);
	    auditRecord.registerQuery(author.getId(), location.getId(), "ESTIMATE_FUTUTE_FORECAST");
        
	    return displayEstimate(locationName, estimatedCondition);
	}

    private List<WeatherRecord> getHistoricalData(LocationRowGateway locationGateway, WeatherRecordGateway weatherRecord, String locationName, int numberOfDays) 
    		throws ApplicationException {
        try {
            Location location = locationGateway.getByName(locationName);

            List<WeatherRecord> records = weatherRecord.getLastNHistoricalByLocation(location.getId(), numberOfDays);

            return records;

        } catch (PersistenceException e) {
        	throw new ApplicationException("Failed to retrieve historical data for location: " + locationName, e);
        }	
    }

    private List<String> extractWeatherConditions(List<WeatherRecord> records) {
        List<String> conditions = new ArrayList<>();

        for (WeatherRecord record : records) {
            WeatherCondition condition = record.getCondition(); // get enum
            conditions.add(condition.toString()); // use description (e.g., "Sun", "Rain")
        }

        return conditions; 
    }

    private String calculateMode(List<String> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return null; // No data available
        }

        Map<String, Integer> frequencyMap = new HashMap<>();

        for (String condition : conditions) {
            frequencyMap.put(condition, frequencyMap.getOrDefault(condition, 0) + 1);
        }

        String mode = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mode = entry.getKey();
            }
        }

        return mode;
    }

    private String displayEstimate(String locationName, String estimatedCondition) {
        if (estimatedCondition == null) {
            return "No sufficient data to estimate future forecast for " + locationName + ".";
        }
        return "The most likely weather condition for " + locationName + " is: " + estimatedCondition;
    }
}
