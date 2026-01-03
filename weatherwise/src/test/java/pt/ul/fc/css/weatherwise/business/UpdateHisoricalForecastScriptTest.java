package pt.ul.fc.css.weatherwise.business;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.DataSource;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuditRecordGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuthorGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.LocationRowGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.WeatherRecordGateway;
import pt.ul.fc.css.weatherwise.entities.WeatherCondition;
import pt.ul.fc.css.weatherwise.entities.WeatherRecord;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UpdateHisoricalForecastScriptTest {

    private UpdateHistoricalForecastScript script = new UpdateHistoricalForecastScript();
    private LocationRowGateway locationGateway = new LocationRowGateway();
    private WeatherRecordGateway weatherRecordGateway = new WeatherRecordGateway();
    private AuditRecordGateway auditRecordGateway = new AuditRecordGateway();
    private AuthorGateway authorGateway = new AuthorGateway();

    private static final String TEST_AUTHOR_NAME = "UpdateTestAuthor";
    private static final String EXISTING_LOCATION = "Braga"; // ID 3
    private static final String NON_EXISTENT_LOCATION = "Neverland";
    
    private static final LocalDate NEW_DATE_LOCAL = LocalDate.of(2025, 11, 10);
    private static final Date NEW_DATE_SQL = Date.valueOf(NEW_DATE_LOCAL);
    
    private static final LocalDate EXISTING_DATE_LOCAL = LocalDate.of(2025, 10, 27);
    private static final Date EXISTING_DATE_SQL = Date.valueOf("2025-10-27");
    
    private static final int BRAGA_ID = 3;


    static {
        System.setProperty("db.url", "jdbc:h2:mem:db_update;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "sa");
    }

    @BeforeAll
    static void setUpAll() {
        DataSource dataSource = DataSource.INSTANCE;
        try {
            dataSource.connect();
            executeSQLFile("_schema.sql");
            executeSQLFile("data.sql");
        } catch (PersistenceException e) {
            fail("FATAL: Não foi possível configurar a base de dados para testes: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() throws PersistenceException {
        String deleteForecastsSql = String.format(
            "DELETE FROM DailyForecast WHERE location_id = %d AND (record_date = DATE '%s' OR record_date = DATE '%s');",
            BRAGA_ID, NEW_DATE_SQL.toString(), EXISTING_DATE_SQL.toString()
        );
        DataSource.INSTANCE.executeSQLUpdate(deleteForecastsSql);
        
        String deleteAuthorSql = String.format("DELETE FROM Author WHERE name = '%s';", TEST_AUTHOR_NAME);
        DataSource.INSTANCE.executeSQLUpdate(deleteAuthorSql);
        
        String reInsertOriginalSql = String.format(
            "INSERT INTO DailyForecast (location_id, record_date, condition_id, temperature) VALUES (%d, DATE '%s', %d, %.1f);",
            BRAGA_ID, EXISTING_DATE_SQL.toString(), 2, 17.0
        );
        DataSource.INSTANCE.executeSQLUpdate(reInsertOriginalSql);
    }
    
    private static void executeSQLFile(String resourcePath) {
        try (InputStream in = LocationRowGateway.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) return;
            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            String[] statements = sql.split(";");
            try (Connection conn = DataSource.INSTANCE.getConnection();
                 Statement stmt = conn.createStatement()) {
                for (String statement : statements) {
                    String trimmed = statement.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }
            }
        } catch (Exception e) {
             fail("Erro ao executar recurso SQL: " + resourcePath + "\n" + e.getMessage());
        }
    }

    
    @Test
    void testUpdateOrInsert_InvalidLocation_ExpectApplicationException() {
        
       
        ApplicationException e = assertThrows(ApplicationException.class, () -> {
            script.updateOrInsertHistoricalForecast(
                locationGateway, weatherRecordGateway, auditRecordGateway, authorGateway, 
                NON_EXISTENT_LOCATION, NEW_DATE_SQL, WeatherCondition.SNOW, 0.0, TEST_AUTHOR_NAME
            );
        }, "Deveria lançar ApplicationException para localização inválida.");
        
        assertTrue(e.getMessage().contains("Location 'Neverland' not found in database."), 
                   "A mensagem de erro deve indicar que a localização não foi encontrada.");
    }
}
