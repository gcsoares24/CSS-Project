package pt.ul.fc.css.weatherwise.business;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.DataSource;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.*;
import pt.ul.fc.css.weatherwise.entities.AuditRecord;
import pt.ul.fc.css.weatherwise.entities.Location;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstimateFutureForecastScriptIntegrationTest {

    private static EstimateFutureForecastScript script;
    private static LocationRowGateway locationGateway;
    private static AuditRecordGateway auditRecordGateway;
    private static AuthorGateway authorGateway;
    
    private static final String LOCATION_FARO = "Faro";    
    private static final String LOCATION_COIMBRA = "Coimbra"; 
    private static final String NON_EXISTENT_LOCATION = "Viseu";
    private static final String TEST_AUTHOR_NAME = "IntegratorUser_Estimate";
    
    private static int faroId;
    private static int coimbraId;
    private static int testAuthorId;

    static {
        System.setProperty("db.url", "jdbc:h2:mem:db_integration;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "sa");
    }

    private static void executeSQLFile(String resourcePath) {
        try (InputStream in = EstimateFutureForecastScriptIntegrationTest.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                fail("Recurso SQL não encontrado: " + resourcePath);
            }
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

    @BeforeAll
    static void setUpAll() throws PersistenceException {
        DataSource.INSTANCE.connect();
        executeSQLFile("_schema.sql"); 
        executeSQLFile("data.sql"); 
        
        locationGateway = new LocationRowGateway();
        auditRecordGateway = new AuditRecordGateway();
        authorGateway = new AuthorGateway();
        script = new EstimateFutureForecastScript();
        
        faroId = locationGateway.getByName(LOCATION_FARO).getId();
        coimbraId = locationGateway.getByName(LOCATION_COIMBRA).getId();
        testAuthorId = authorGateway.findOrCreateByName(TEST_AUTHOR_NAME).getId(); 
    }


    // -------------------------------------------------------------------
    //                             TESTES
    // -------------------------------------------------------------------


    @Test
    void testExecute_Faro_15Days_ExpectSun() throws ApplicationException, PersistenceException {

        final int numberOfDays = 15;
        
        String result = script.execute(locationGateway, new WeatherRecordGateway(), auditRecordGateway, authorGateway,
                LOCATION_FARO, numberOfDays, TEST_AUTHOR_NAME);

        assertTrue(result.contains("Sun"), "A previsão estimada para Faro (15 dias) deveria ser 'Sun'.");
        
        List<AuditRecord> auditRecords = auditRecordGateway.findAll();
        boolean isAudited = auditRecords.stream()
                .anyMatch(a -> a.getAuthorId() == testAuthorId && 
                             a.getLocationId() == faroId &&
                             a.getQueryType().equals("ESTIMATE_FUTUTE_FORECAST"));
                
        assertTrue(isAudited, "Deve existir um registo de auditoria para esta consulta.");
    }
    

    @Test
    void testExecute_Coimbra_10Days_ExpectRain() throws ApplicationException, PersistenceException {
        final int numberOfDays = 10;

        String result = script.execute(locationGateway, new WeatherRecordGateway(), auditRecordGateway, authorGateway,
                LOCATION_COIMBRA, numberOfDays, TEST_AUTHOR_NAME);

        assertTrue(result.contains("Rain"), "A previsão estimada para Coimbra (30 dias) deveria ser 'Rain'.");
        
        List<AuditRecord> auditRecords = auditRecordGateway.findAll();
        boolean isAudited = auditRecords.stream()
                .anyMatch(a -> a.getAuthorId() == testAuthorId && 
                             a.getLocationId() == coimbraId &&
                             a.getQueryType().equals("ESTIMATE_FUTUTE_FORECAST"));
                             
        assertTrue(isAudited, "Deve existir um registo de auditoria para esta consulta.");
    }


    
    
    @Test
    void testExecute_Lisboa_1Day_ExpectSun() throws ApplicationException, PersistenceException {
        
        final int locIdLisboa = locationGateway.getByName("Lisboa").getId();
        
        String result = script.execute(locationGateway, new WeatherRecordGateway(), auditRecordGateway, authorGateway,
                "Lisboa", 5, TEST_AUTHOR_NAME);
        
        assertTrue(result.contains("Sun"), "A previsão estimada para Lisboa (5 dias) deveria ser 'Sun'.");
    }
}
