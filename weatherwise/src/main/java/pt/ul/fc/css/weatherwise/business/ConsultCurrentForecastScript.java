package pt.ul.fc.css.weatherwise.business;

import java.time.LocalDateTime;

import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.LocationRowGateway; 
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.WeatherRecordGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuditRecordGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuthorGateway; 
import pt.ul.fc.css.weatherwise.entities.Location;
import pt.ul.fc.css.weatherwise.entities.WeatherRecord;
import pt.ul.fc.css.weatherwise.entities.Author; 
import pt.ul.fc.css.weatherwise.entities.AuditRecord; 

/**
 * ConsultCurrentForecastScript: Transaction Script for Use Case A.
 * Coordinates data retrieval (current forecast) and audit logging.
 */
public class ConsultCurrentForecastScript {
 
    /**
     * Executes Use Case A: Finds the current forecast by location name and logs the query.
     */
    public String getCurrentForecast(LocationRowGateway locationGateway, WeatherRecordGateway weatherRecordGateway,  AuditRecordGateway auditRecordGateway,AuthorGateway authorGateway, String locationName, String authorName) 
            throws ApplicationException, PersistenceException {
        
        
        Location location = locationGateway.getByName(locationName);

        if(location == null) {
            throw new ApplicationException("Invalid Location: Location '" + locationName + "' not found in database.");
        }
        
        int locId = location.getId();
        
        WeatherRecord currentRecord = weatherRecordGateway.getLastByLocation(locId);

        if (currentRecord == null) {
            return "There is no forecast prevision for today (" + locationName + ").";
        }
        
        // audit, create/find author AND insert audit in history
        Author author = authorGateway.findOrCreateByName(authorName);
        auditRecordGateway.registerQuery(author.getId(), locId, "CURRENT_FORECAST"); 

        
        String resultCondition = currentRecord.getCondition().name(); // enum name
        double resultTemp = currentRecord.getTemperature();

        return String.format(
            "The current forecast for %s is: %s, with a temperature of %.1f °C.", 
            locationName, 
            resultCondition, 
            resultTemp
        );
    }
    
  
}