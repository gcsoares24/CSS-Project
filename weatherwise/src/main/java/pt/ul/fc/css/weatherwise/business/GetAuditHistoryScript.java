package pt.ul.fc.css.weatherwise.business;

import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuditRecordGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuthorGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.LocationRowGateway;
import pt.ul.fc.css.weatherwise.entities.AuditRecord;
import pt.ul.fc.css.weatherwise.entities.Author;
import pt.ul.fc.css.weatherwise.entities.Location;

import java.util.List;


public class GetAuditHistoryScript {



    public String getAllHistory(LocationRowGateway locationGateway, AuditRecordGateway auditGateway, AuthorGateway authorGateway, String authorName) throws PersistenceException, ApplicationException {
       
        
        List<AuditRecord> audit = auditGateway.findAll();
    	
    
        // Convert audit records to a single string
        StringBuilder sb = new StringBuilder();
        for (AuditRecord record : audit) {
            sb.append("Audit ID: ").append(record.getId())
              .append(", Author ID: ").append(record.getAuthorId())
              .append(", Location ID: ").append(record.getLocationId())
              .append(", Query Type: ").append(record.getQueryType())
              .append(", Timestamp: ").append(record.getQueryTimestamp())
              .append("\n");
        }

        return sb.toString();
    }
}