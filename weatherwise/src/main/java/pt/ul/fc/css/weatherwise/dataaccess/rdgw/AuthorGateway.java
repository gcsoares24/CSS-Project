package pt.ul.fc.css.weatherwise.dataaccess.rdgw;

import pt.ul.fc.css.weatherwise.dataaccess.DataSource;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.entities.Author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthorGateway {
    private static final String FIND_BY_NAME = 
        "SELECT * " +
        "FROM Author " + 
        "WHERE name = ?;";

    private static final String INSERT_SQL =
        "INSERT INTO Author (name) VALUES (?);";

    public Author findByName(String name) throws PersistenceException {
        try (Connection conn = DataSource.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_NAME)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return load(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error searching by name: " + name, e);
        }
    }
    public Author findOrCreateByName(String name) throws PersistenceException {
        try {
            DataSource.INSTANCE.beginTransaction();

            Author author = null;

            try (PreparedStatement ps = DataSource.INSTANCE.prepare(FIND_BY_NAME)) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        author = load(rs);
                    }
                }
            }

            if (author != null) {
                DataSource.INSTANCE.commit(); 
                return author;
            }

            try (PreparedStatement ps = DataSource.INSTANCE.prepareGetGenKey(INSERT_SQL)) {
                ps.setString(1, name);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1);
                        author = new Author(newId, name);
                    } else {
                        throw new PersistenceException("Error finding a generated ID for the new author: " + name);
                    }
                }
            }

            DataSource.INSTANCE.commit(); 
            return author;

        } catch (Exception e) {
            throw new PersistenceException("Error finding or creating the author: " + name, e);
        }
    }
    private Author load(ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setId(rs.getInt("id"));
        author.setName(rs.getString("name"));
        return author;
    }
}
