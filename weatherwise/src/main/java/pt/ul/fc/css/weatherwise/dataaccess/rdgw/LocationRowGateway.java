package pt.ul.fc.css.weatherwise.dataaccess.rdgw;

import pt.ul.fc.css.weatherwise.dataaccess.DataSource;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.entities.Location;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LocationRowGateway {

    private static final String FIND_BY_NAME = "SELECT id, name FROM Location WHERE name = ?";

    public Location getByName(String name) throws PersistenceException {
        try (Connection connection = DataSource.INSTANCE.getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_BY_NAME)) {

            ps.setString(1, name);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                	
                    return new Location(rs.getInt("id"), rs.getString("name"));
                }
            }
            return null; 
        } catch (SQLException e) {
            throw new PersistenceException("Error finding location by name: " + name, e);
        }
    }
}