package pt.ul.fc.css.weatherwise.weatherwise;

import pt.ul.fc.css.weatherwise.presentation.ForecastService;

import pt.ul.fc.css.weatherwise.presentation.AuditService;
import pt.ul.fc.css.weatherwise.presentation.HistoryService;
import pt.ul.fc.css.weatherwise.business.ConsultCurrentForecastScript;
import pt.ul.fc.css.weatherwise.business.EstimateFutureForecastScript;
import pt.ul.fc.css.weatherwise.business.GetAuditHistoryScript;
import pt.ul.fc.css.weatherwise.business.UpdateHistoricalForecastScript;
import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.DataSource;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;

public class WeatherWise {

    private ForecastService forecastService;
    private AuditService auditService;
    private HistoryService historyService;

    public void run() throws ApplicationException {
        try {
            DataSource.INSTANCE.connect();
            forecastService = new ForecastService(new ConsultCurrentForecastScript(), new UpdateHistoricalForecastScript());
            auditService = new AuditService(new GetAuditHistoryScript());
            historyService = new HistoryService(new EstimateFutureForecastScript());
            
        } catch (PersistenceException e) {
            throw new ApplicationException("Error connecting to database", e);
        }
    }

    public void stopRun() {
        DataSource.INSTANCE.close();
    }

    public ForecastService getForecastService() {
        return forecastService;
    }

    public AuditService getAuditService() {
        return auditService;
    }
    
    public HistoryService getHistoryService() {
        return historyService;
    }
}
