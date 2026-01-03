package pt.ul.fc.css.weatherwise.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuditRecordGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuthorGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.LocationRowGateway;
import pt.ul.fc.css.weatherwise.entities.AuditRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAuditHistoryScriptUnitTest {

    private GetAuditHistoryScript script;
    
    // Mocks para as dependências
    private AuditRecordGateway mockAuditGateway;
    private LocationRowGateway mockLocationGateway;
    private AuthorGateway mockAuthorGateway;    

    private static final String AUTHOR_NAME = "TestUser";

    @BeforeEach
    void setUp() {
        script = new GetAuditHistoryScript();
        mockAuditGateway = mock(AuditRecordGateway.class);
        mockLocationGateway = mock(LocationRowGateway.class);
        mockAuthorGateway = mock(AuthorGateway.class);
    }

    @Test
    void testGetAllHistory_ReturnsFormattedString() throws PersistenceException, ApplicationException {
        LocalDateTime timestamp1 = LocalDateTime.of(2025, 11, 1, 10, 0, 0);
        LocalDateTime timestamp2 = LocalDateTime.of(2025, 11, 2, 15, 30, 0);

        AuditRecord record1 = new AuditRecord();
        record1.setId(101L);
        record1.setAuthorId(10);
        record1.setLocationId(1);
        record1.setQueryType("CURRENT_FORECAST");
        record1.setQueryTimestamp(timestamp1);

        AuditRecord record2 = new AuditRecord();
        record2.setId(102L);
        record2.setAuthorId(20);
        record2.setLocationId(5);
        record2.setQueryType("ESTIMATE_FUTUTE_FORECAST");
        record2.setQueryTimestamp(timestamp2);
        
        List<AuditRecord> mockAuditList = Arrays.asList(record2, record1); 

        when(mockAuditGateway.findAll()).thenReturn(mockAuditList);

        String result = script.getAllHistory(mockLocationGateway, mockAuditGateway, mockAuthorGateway, AUTHOR_NAME);

        String expectedLine1 = String.format("Audit ID: 102, Author ID: 20, Location ID: 5, Query Type: ESTIMATE_FUTUTE_FORECAST, Timestamp: %s\n", timestamp2);
        String expectedLine2 = String.format("Audit ID: 101, Author ID: 10, Location ID: 1, Query Type: CURRENT_FORECAST, Timestamp: %s\n", timestamp1);
        
        String expectedResult = expectedLine1 + expectedLine2;
        
        assertEquals(expectedResult, result, "A string formatada deve incluir todos os registos mockados.");

        verify(mockAuditGateway, times(1)).findAll();
    }
    
    @Test
    void testGetAllHistory_EmptyList_ReturnsEmptyString() throws PersistenceException, ApplicationException {
        
        List<AuditRecord> mockAuditList = Arrays.asList(); 
        when(mockAuditGateway.findAll()).thenReturn(mockAuditList);

       
        String result = script.getAllHistory(mockLocationGateway, mockAuditGateway, mockAuthorGateway, AUTHOR_NAME);

        
        assertTrue(result.isEmpty(), "Deve retornar uma string vazia quando não há registos.");

        
        verify(mockAuditGateway, times(1)).findAll();
    }

  
    @Test
    void testGetAllHistory_PersistenceExceptionThrown() throws PersistenceException {
      
        when(mockAuditGateway.findAll()).thenThrow(new PersistenceException("DB connection failed"));

        assertThrows(PersistenceException.class, () -> {
            script.getAllHistory(mockLocationGateway, mockAuditGateway, mockAuthorGateway, AUTHOR_NAME);
        }, "Deveria propagar a PersistenceException lançada pelo Gateway.");

        verify(mockAuditGateway, times(1)).findAll();
    }
}
