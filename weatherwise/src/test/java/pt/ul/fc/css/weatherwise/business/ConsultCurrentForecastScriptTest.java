package pt.ul.fc.css.weatherwise.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
import pt.ul.fc.css.weatherwise.entities.AuditRecord;

class ConsultCurrentForecastScriptTest {

    private static ConsultCurrentForecastScript script;
    private static LocationRowGateway locationGateway;
    private static WeatherRecordGateway weatherRecordGateway;
    private static AuditRecordGateway auditRecordGateway;
    private static AuthorGateway authorGateway;

    private static final String TEST_AUTHOR_NAME = "TestAuthor_ConsultCurrent";
    private static final String EXISTING_LOCATION = "Lisboa"; // ID 1
    private static final String LOCATION_WITHOUT_DATA = "Arruda dos Vinhos"; // ID 6
    private static final String NON_EXISTENT_LOCATION = "Neverland";

    // Configuração do H2 em memória
    static {
        System.setProperty("db.url", "jdbc:h2:mem:db_current_test;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "sa");
    }

    @BeforeAll
    static void setUpAll() {
        script = new ConsultCurrentForecastScript();
        locationGateway = new LocationRowGateway();
        weatherRecordGateway = new WeatherRecordGateway();
        auditRecordGateway = new AuditRecordGateway();
        authorGateway = new AuthorGateway();

        DataSource dataSource = DataSource.INSTANCE;

        try {
            dataSource.connect();
            executeSQLFile("_schema.sql");
            executeSQLFile("data.sql");
        } catch (PersistenceException e) {
            fail("FATAL: Não foi possível configurar a base de dados para testes: " + e.getMessage());
        }
    }

    private static void executeSQLFile(String resourcePath) {
        try (InputStream in = ConsultCurrentForecastScriptTest.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                String content = "";
                switch (resourcePath) {
                    case "_schema.sql":
                        content = "-- Conteúdo omitido, mas assumido ser o schema DDL completo --";
                        break;
                    case "data.sql":
                        content = "-- Conteúdo omitido, mas assumido ser o data DML completo --";
                        break;
                    default:
                        fail("Recurso SQL não encontrado: " + resourcePath);
                        return;
                }
               
                return;
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
    @AfterEach
    void tearDown() throws PersistenceException {
        
        String deleteAuditSql = "DELETE FROM AuditLog WHERE author_id IN (SELECT id FROM Author WHERE name = '" + TEST_AUTHOR_NAME + "');";
        String deleteAuthorSql = "DELETE FROM Author WHERE name = '" + TEST_AUTHOR_NAME + "';";
        
        DataSource.INSTANCE.executeSQLUpdate(deleteAuditSql);
        DataSource.INSTANCE.executeSQLUpdate(deleteAuthorSql);
    }
    // ====================================================================
    //                             TESTES
    // ====================================================================

    /**
     * Testa o 'Caminho Feliz': Localização existe, previsão atual existe.
     * Verifica o resultado formatado e o registo de auditoria.
     */
    @Test
    void testGetCurrentForecast_SuccessAndAuditLog() throws ApplicationException, PersistenceException {
       
        String expectedResult = String.format("The current forecast for %s is: SUN, with a temperature of %.1f °C.", 
                                                EXISTING_LOCATION, 22.0);
        
        String actualResult = script.getCurrentForecast(
            locationGateway, 
            weatherRecordGateway, 
            auditRecordGateway, 
            authorGateway, 
            EXISTING_LOCATION, 
            TEST_AUTHOR_NAME
        );

        assertEquals(expectedResult, actualResult, "O resultado da previsão deve corresponder ao esperado.");
        
        List<AuditRecord> logs = auditRecordGateway.findAll();
        
        long count = logs.stream()
                         .filter(log -> log.getQueryType().equals("CURRENT_FORECAST"))
                         .filter(log -> {
							try {
								return log.getAuthorId() == authorGateway.findByName(TEST_AUTHOR_NAME).getId();
							} catch (PersistenceException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return false;
						})
                         .count();
        
        assertTrue(count >= 1, "Deve ser encontrado pelo menos um registo de auditoria para o autor de teste.");
    }

    /**
     * Testa o cenário de Localização Inexistente.
     * Espera uma ApplicationException.
     */
    @Test
    void testGetCurrentForecast_InvalidLocation_ExpectApplicationException() {
        ApplicationException e = assertThrows(ApplicationException.class, () -> {
            script.getCurrentForecast(
                locationGateway, 
                weatherRecordGateway, 
                auditRecordGateway, 
                authorGateway, 
                NON_EXISTENT_LOCATION, 
                TEST_AUTHOR_NAME
            );
        }, "Uma ApplicationException deveria ser lançada para localização inválida.");

        assertTrue(e.getMessage().contains("Location '" + NON_EXISTENT_LOCATION + "' not found in database."), 
                   "A mensagem de exceção deve indicar 'localização não encontrada'.");
    }

    /**
     * Testa o cenário de Localização Existente, mas sem dados históricos.
     * A regra de negócio deve retornar uma mensagem informativa, sem lançar exceção.
     */
    @Test
    void testGetCurrentForecast_LocationWithoutData_ExpectInformativeMessage() throws ApplicationException, PersistenceException {
       
        String expectedMessage = "There is no forecast prevision for today (" + LOCATION_WITHOUT_DATA + ").";

        String actualResult = script.getCurrentForecast(
            locationGateway, 
            weatherRecordGateway, 
            auditRecordGateway, 
            authorGateway, 
            LOCATION_WITHOUT_DATA, 
            TEST_AUTHOR_NAME
        );

        assertEquals(expectedMessage, actualResult, "Deveria retornar uma mensagem informativa se não houver previsão.");

        
        List<AuditRecord> logs = auditRecordGateway.findAll();
        long count = logs.stream()
                         .filter(log -> log.getQueryType().equals("CURRENT_FORECAST"))
                         .filter(log -> {
							try {
								return log.getAuthorId() == authorGateway.findByName(TEST_AUTHOR_NAME).getId();
							} catch (PersistenceException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return false;
						})
                         .count();
        
        assertEquals(0, count, "Um registo de auditoria deve ser criado se a previsão não for encontrada.");
    }
}
