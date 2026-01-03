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

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class GetAuditHistoryScriptTest {

    private GetAuditHistoryScript script = new GetAuditHistoryScript();
    private LocationRowGateway locationGateway = new LocationRowGateway();
    private AuditRecordGateway auditGateway = new AuditRecordGateway();
    private AuthorGateway authorGateway = new AuthorGateway();
    private static final String TEST_AUTHOR_NAME = "TestUser"; 

    static {
        System.setProperty("db.url", "jdbc:h2:mem:db_audit;DB_CLOSE_DELAY=-1");
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
    void tearDown() {
    }

    private static void executeSQLFile(String resourcePath) {
        try (InputStream in = LocationRowGateway.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
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
    

    @Test
    void testGetAllHistory_SuccessAndFormat_DataOnly() throws PersistenceException, ApplicationException {
        
        String history = script.getAllHistory(locationGateway, auditGateway, authorGateway, TEST_AUTHOR_NAME);
        
        long expectedLogCount = 11;
        long lineCount = history.chars().filter(ch -> ch == '\n').count();

        
        assertEquals(expectedLogCount, lineCount, "O histórico deve conter o número correto de 11 logs (linhas) do data.sql.");

        String expectedRecentLogSnippet = "Location ID: 5, Query Type: HISTORICAL, Timestamp: 2025-10-31T12:00";

        assertTrue(history.startsWith("Audit ID: "), "A string deve começar com a formatação de Audit ID.");
        assertTrue(history.contains(expectedRecentLogSnippet), 
                   "O log mais recente (Coimbra, 12:00) deve estar presente e na formatação correta.");
        
        assertTrue(history.contains("Location ID: 2, Query Type: CURRENT, Timestamp: 2025-10-30T09:15"), 
                   "Um log conhecido dos dados iniciais (Porto) deve estar presente.");
    }
}
