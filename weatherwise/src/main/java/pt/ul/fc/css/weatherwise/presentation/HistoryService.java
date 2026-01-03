package pt.ul.fc.css.weatherwise.presentation;

import pt.ul.fc.css.weatherwise.business.EstimateFutureForecastScript;
import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuditRecordGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuthorGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.LocationRowGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.WeatherRecordGateway;

public class HistoryService {
	private EstimateFutureForecastScript estimateForecastTS;
	
	public HistoryService(EstimateFutureForecastScript estimateForecastTS) {
		this.estimateForecastTS = estimateForecastTS;
	}
	
	public String getForecastEstimation(String locationName, int numberOfDays, String authorName) throws ApplicationException, PersistenceException {
		return estimateForecastTS.execute(new LocationRowGateway(), new WeatherRecordGateway(), new AuditRecordGateway(), new AuthorGateway(),locationName, numberOfDays, authorName);
    }
}
