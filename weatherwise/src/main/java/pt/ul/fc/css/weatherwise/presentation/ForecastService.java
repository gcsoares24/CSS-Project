package pt.ul.fc.css.weatherwise.presentation;

import java.sql.Date;
import java.time.LocalDate;

import pt.ul.fc.css.weatherwise.business.ConsultCurrentForecastScript;

import pt.ul.fc.css.weatherwise.business.UpdateHistoricalForecastScript;
import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuditRecordGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuthorGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.LocationRowGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.WeatherRecordGateway;
import pt.ul.fc.css.weatherwise.entities.WeatherCondition;
public class ForecastService {
	private ConsultCurrentForecastScript consultForecastTS;
	private UpdateHistoricalForecastScript updateForecastTS;
	
	public ForecastService(ConsultCurrentForecastScript consultForecastTS,
			UpdateHistoricalForecastScript updateForecastTS) {
		this.consultForecastTS = consultForecastTS;
		this.updateForecastTS = updateForecastTS;
	}
	public String getCurrentForecast(String location, String authorName) throws ApplicationException, PersistenceException {
		return consultForecastTS.getCurrentForecast(new LocationRowGateway(), new WeatherRecordGateway(), new AuditRecordGateway(), new AuthorGateway(), location, authorName);
	}
	
	public String getUpdateForecastTS(String locationName, Date date, WeatherCondition wc, Double temperature, String authorName) throws ApplicationException, PersistenceException {
		return updateForecastTS.updateOrInsertHistoricalForecast(new LocationRowGateway(),new WeatherRecordGateway(), new AuditRecordGateway(), new AuthorGateway(), locationName, date, wc, temperature, authorName);
	}
	
}