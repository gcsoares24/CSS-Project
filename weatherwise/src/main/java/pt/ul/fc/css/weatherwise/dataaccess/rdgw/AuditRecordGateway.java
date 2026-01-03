package pt.ul.fc.css.weatherwise.dataaccess.rdgw;

import pt.ul.fc.css.weatherwise.dataaccess.DataSource;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.entities.AuditRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class AuditRecordGateway {


    private static final String INSERT_SQL = "INSERT INTO AuditLog (author_id, location_id, query_type) VALUES (?, ?, ?)";


    public void registerQuery(int author_id, int location_id, String query_type) throws PersistenceException {
        try (Connection conn = DataSource.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setInt(1, author_id);
            ps.setInt(2, location_id);
            ps.setString(3, query_type);
            
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException("Error registering audit query", e);
        }
    }


    private static final String SELECT_ALL_SQL = "SELECT * FROM AuditLog ORDER BY query_timestamp DESC";

    public List<AuditRecord> findAll() throws PersistenceException {
        List<AuditRecord> logs = new ArrayList<>();

        try (Connection conn = DataSource.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                logs.add(loadAuditRecord(rs)); 
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error listing audit history", e);
        }
        return logs;
    }


    private AuditRecord loadAuditRecord(ResultSet rs) throws SQLException {
        AuditRecord record = new AuditRecord();
        
        record.setId(rs.getLong("id"));
        record.setAuthorId(rs.getInt("author_id"));
        record.setLocationId(rs.getInt("location_id"));
        
        Timestamp ts = rs.getTimestamp("query_timestamp");
        if (ts != null) {
            record.setQueryTimestamp(ts.toLocalDateTime());
        }
        
        record.setQueryType(rs.getString("query_type"));
        
        return record;
    }
}
