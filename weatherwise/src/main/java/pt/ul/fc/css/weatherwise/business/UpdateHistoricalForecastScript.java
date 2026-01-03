package pt.ul.fc.css.weatherwise.business;

import java.sql.Date;
import java.time.LocalDate;

import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuditRecordGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuthorGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.LocationRowGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.WeatherRecordGateway;
import pt.ul.fc.css.weatherwise.entities.Author;
import pt.ul.fc.css.weatherwise.entities.Location;
import pt.ul.fc.css.weatherwise.entities.WeatherCondition;
import pt.ul.fc.css.weatherwise.entities.WeatherRecord;

public class UpdateHistoricalForecastScript {
	public String updateOrInsertHistoricalForecast(LocationRowGateway locationGateway, WeatherRecordGateway weatherRecordGateway,  AuditRecordGateway auditRecordGateway,AuthorGateway authorGateway, String locationName, Date date, WeatherCondition wc, Double temperature, String authorName) throws ApplicationException, PersistenceException {
		//Does the location exist?
        Location location = locationGateway.getByName(locationName);

        if(location == null) {
            throw new ApplicationException("Invalid Location: Location '" + locationName + "' not found in database.");
        }
        
        int locId = location.getId();        
        
        //if null, it'll insert, otherwise it'll update
        
        WeatherRecord wr = weatherRecordGateway.getWeatherByLocationAndDate(locId, date);
        
        int id = weatherRecordGateway.addForecast(locId, date, wc, temperature);

    	Author author = authorGateway.findOrCreateByName(authorName);
    	String query;
        if(wr == null) {
        	query = "INSERT_FORECAST";
        }else {
        	query = "UPDATE_FORECAST";
        	
        }
        auditRecordGateway.registerQuery(author.getId(), locId, query); 

		return "The DailyForecast with id=" + id + " has been updated/inserted";
		
	}

}
