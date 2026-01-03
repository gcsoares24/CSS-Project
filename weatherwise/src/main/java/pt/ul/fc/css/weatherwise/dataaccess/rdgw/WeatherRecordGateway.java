package pt.ul.fc.css.weatherwise.dataaccess.rdgw;

import pt.ul.fc.css.weatherwise.dataaccess.DataSource;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.entities.WeatherRecord;
import pt.ul.fc.css.weatherwise.entities.WeatherCondition; // Necessary for Enum mapping

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class WeatherRecordGateway {

    private static final String FIND_LAST_N_HISTORICAL_BY_LOCATION = 
        "SELECT record_date, condition_id, temperature " +
        "FROM DailyForecast " +
        "WHERE location_id = ? AND record_date <= CURRENT_DATE " +
        "ORDER BY record_date DESC LIMIT ?";

    private static final String FIND_BY_LOCATION_AND_DATE = "SELECT record_date, condition_id, temperature " +
            "FROM DailyForecast " +
            "WHERE location_id = ? AND record_date = ?";
    
    private static final String UPSERT_FORECAST = "INSERT INTO DailyForecast (location_id, record_date, condition_id, temperature) " +
            "VALUES (?, ?, ?, ?) " +
            "ON CONFLICT (location_id, record_date) " +
            "DO UPDATE SET condition_id = EXCLUDED.condition_id, temperature = EXCLUDED.temperature " +
            "RETURNING id";

    /**
     * Devolve os últimos N registos históricos (até à data atual).
     * Se numberOfDays == 1, funciona como getLastByLocation.
     */
    public List<WeatherRecord> getLastNHistoricalByLocation(int locationId, int numberOfDays) throws PersistenceException {
        List<WeatherRecord> records = new ArrayList<>();
        
        try (Connection connection = DataSource.INSTANCE.getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_LAST_N_HISTORICAL_BY_LOCATION)) {

            ps.setInt(1, locationId);
            ps.setInt(2, numberOfDays);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.sql.Date sqlDate = rs.getDate("record_date");
                    int conditionId = rs.getInt("condition_id");
                    double temperature = rs.getDouble("temperature");

                    WeatherCondition condition = WeatherCondition.fromId(conditionId);
                    java.time.LocalDate recordDate = sqlDate.toLocalDate(); 

                    WeatherRecord record = new WeatherRecord(locationId, recordDate, condition, temperature);
                    records.add(record);
                }
            }

            return records;

        } catch (SQLException e) {
            throw new PersistenceException("Error finding last historical weather records for location ID: " + locationId, e);
        }
    }

    /**
     * Devolve o último registo histórico (até à data atual).
     * Chama getLastNHistoricalByLocation com numberOfDays = 1.
     */
    public WeatherRecord getLastByLocation(int locationId) throws PersistenceException {
        List<WeatherRecord> records = getLastNHistoricalByLocation(locationId, 1);

        if (records.isEmpty()) {
            return null;
        } else {
            return records.get(0);
        }
    }
    
    public WeatherRecord getWeatherByLocationAndDate(int locationId, Date date) {
      
        
        try (Connection connection = DataSource.INSTANCE.getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_BY_LOCATION_AND_DATE)) {

            ps.setInt(1, locationId); 
            ps.setDate(2, date); 

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                	Date sqlDate = rs.getDate("record_date");
                    int conditionId = rs.getInt("condition_id");
                    double temperature = rs.getDouble("temperature");

                    WeatherCondition condition = WeatherCondition.fromId(conditionId);
                    LocalDate recordDate = sqlDate.toLocalDate();

                    WeatherRecord record = new WeatherRecord(locationId, recordDate, condition, temperature);
                    return record;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public int addForecast(int locationId, Date date, WeatherCondition condition, double temperature) {
        
        

        try (Connection connection = DataSource.INSTANCE.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPSERT_FORECAST)) {

           
            ps.setInt(1, locationId); 
            ps.setDate(2, date);
            ps.setInt(3, condition.getId());
            ps.setDouble(4, temperature);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int generatedId = rs.getInt("id");

                    return generatedId;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (Integer) null; 
    }


}