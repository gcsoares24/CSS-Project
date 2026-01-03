package pt.ul.fc.css.weatherwise.presentation;

import pt.ul.fc.css.weatherwise.business.GetAuditHistoryScript;
import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuditRecordGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuthorGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.LocationRowGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.WeatherRecordGateway;
import pt.ul.fc.css.weatherwise.entities.AuditRecord;

import java.util.List;
public class AuditService {
	public GetAuditHistoryScript getAuditHistoryScript;
    
	public AuditService(GetAuditHistoryScript getAuditHistoryScript) {
		this.getAuditHistoryScript = getAuditHistoryScript;
	}
	public String getAllHistoryRecords(String authorName) throws PersistenceException, ApplicationException {
        return getAuditHistoryScript.getAllHistory(new LocationRowGateway(), new AuditRecordGateway (),new AuthorGateway(), authorName);
    }
}
