package pt.ul.fc.css.weatherwise.presentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pt.ul.fc.css.weatherwise.business.GetAuditHistoryScript;
import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuditRecordGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuthorGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.LocationRowGateway;

class AuditServiceUnitTest {

    @Mock
    private GetAuditHistoryScript mockScript;

    private AuditService auditService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        auditService = new AuditService(mockScript);
    }

    @Test
    void testGetAllHistoryRecordsReturnsFormattedString() throws Exception {
        String authorName = "Alice";
        String expectedResult = "Audit record string";

        when(mockScript.getAllHistory(
                any(LocationRowGateway.class),
                any(AuditRecordGateway.class),
                any(AuthorGateway.class),
                eq(authorName)
        )).thenReturn(expectedResult);

        String result = auditService.getAllHistoryRecords(authorName);

        assertEquals(expectedResult, result);
        verify(mockScript, times(1)).getAllHistory(
                any(LocationRowGateway.class),
                any(AuditRecordGateway.class),
                any(AuthorGateway.class),
                eq(authorName)
        );
    }

    @Test
    void testGetAllHistoryRecordsThrowsPersistenceException() throws Exception {
        String authorName = "Bob";

        when(mockScript.getAllHistory(
                any(LocationRowGateway.class),
                any(AuditRecordGateway.class),
                any(AuthorGateway.class),
                eq(authorName)
        )).thenThrow(new PersistenceException("DB error"));

        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            auditService.getAllHistoryRecords(authorName);
        });

        assertEquals("DB error", exception.getMessage());
    }

    @Test
    void testGetAllHistoryRecordsThrowsApplicationException() throws Exception {
        String authorName = "Charlie";

        when(mockScript.getAllHistory(
                any(LocationRowGateway.class),
                any(AuditRecordGateway.class),
                any(AuthorGateway.class),
                eq(authorName)
        )).thenThrow(new ApplicationException("App error"));

        ApplicationException exception = assertThrows(ApplicationException.class, () -> {
            auditService.getAllHistoryRecords(authorName);
        });

        assertEquals("App error", exception.getMessage());
    }
    
    @Test
    void testGetAllHistoryRecordsWithEmptyAuthorName() throws Exception {
        String authorName = "";
        String expectedResult = "Some audit data";

 
        when(mockScript.getAllHistory(
                any(LocationRowGateway.class),
                any(AuditRecordGateway.class),
                any(AuthorGateway.class),
                eq(authorName)
        )).thenReturn(expectedResult);


        String result = auditService.getAllHistoryRecords(authorName);

        assertEquals(expectedResult, result, "Service should propagate the string even if authorName is empty");
        verify(mockScript, times(1)).getAllHistory(any(LocationRowGateway.class),
                                                    any(AuditRecordGateway.class),
                                                    any(AuthorGateway.class),
                                                    eq(authorName));
    }

    @Test
    void testGetAllHistoryRecordsWithNullAuthorName() throws Exception {
        String authorName = null; // null author
        String expectedResult = "Some audit data";


        when(mockScript.getAllHistory(
                any(LocationRowGateway.class),
                any(AuditRecordGateway.class),
                any(AuthorGateway.class),
                eq(authorName)
        )).thenReturn(expectedResult);

    
        String result = auditService.getAllHistoryRecords(authorName);

       
        assertEquals(expectedResult, result, "Service should propagate the string even if authorName is null");
        verify(mockScript, times(1)).getAllHistory(any(LocationRowGateway.class),
                                                    any(AuditRecordGateway.class),
                                                    any(AuthorGateway.class),
                                                    eq(authorName));
    }

    @Test
    void testGetAllHistoryRecordsWithEmptyAuditList() throws Exception {
        String authorName = "Dave";

        when(mockScript.getAllHistory(
                any(LocationRowGateway.class),
                any(AuditRecordGateway.class),
                any(AuthorGateway.class),
                eq(authorName)
        )).thenReturn("");

        String result = auditService.getAllHistoryRecords(authorName);

  
        assertEquals("", result, "Service should return null if no audit records exist");
        verify(mockScript, times(1)).getAllHistory(any(LocationRowGateway.class),
                                                    any(AuditRecordGateway.class),
                                                    any(AuthorGateway.class),
                                                    eq(authorName));
    }
}
