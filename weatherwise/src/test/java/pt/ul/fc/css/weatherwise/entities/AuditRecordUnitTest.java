package pt.ul.fc.css.weatherwise.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class AuditRecordUnitTest {

    @Test
    void testConstructorAndGetters() {
        AuditRecord record = new AuditRecord(1, 100, "ESTIMATION");

        assertEquals(1, record.getAuthorId(), "Author ID should match constructor value");
        assertEquals(100, record.getLocationId(), "Location ID should match constructor value");
        assertEquals("ESTIMATION", record.getQueryType(), "Query type should match constructor value");
        assertNull(record.getId(), "ID should be null when not set");
        assertNull(record.getQueryTimestamp(), "Query timestamp should be null when not set");
    }

    @Test
    void testDefaultConstructorAndSetters() {
        AuditRecord record = new AuditRecord();

        record.setId(123L);
        record.setAuthorId(5);
        record.setLocationId(50);
        LocalDateTime now = LocalDateTime.now();
        record.setQueryTimestamp(now);
        record.setQueryType("CURRENT_FORECAST");

        assertEquals(123L, record.getId(), "ID should be set correctly");
        assertEquals(5, record.getAuthorId(), "Author ID should be set correctly");
        assertEquals(50, record.getLocationId(), "Location ID should be set correctly");
        assertEquals(now, record.getQueryTimestamp(), "Query timestamp should be set correctly");
        assertEquals("CURRENT_FORECAST", record.getQueryType(), "Query type should be set correctly");
    }

    @Test
    void testMutability() {
        AuditRecord record = new AuditRecord(2, 200, "HISTORICAL");

        record.setId(999L);
        record.setAuthorId(10);
        record.setLocationId(500);
        LocalDateTime later = LocalDateTime.now().plusDays(1);
        record.setQueryTimestamp(later);
        record.setQueryType("ESTIMATION");

        assertEquals(999L, record.getId());
        assertEquals(10, record.getAuthorId());
        assertEquals(500, record.getLocationId());
        assertEquals(later, record.getQueryTimestamp());
        assertEquals("ESTIMATION", record.getQueryType());
    }
}