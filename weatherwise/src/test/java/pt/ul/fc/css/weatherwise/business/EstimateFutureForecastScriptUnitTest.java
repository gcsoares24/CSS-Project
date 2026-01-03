package pt.ul.fc.css.weatherwise.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.*;
import pt.ul.fc.css.weatherwise.entities.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstimateFutureForecastScriptUnitTest {

    private EstimateFutureForecastScript script;
    private LocationRowGateway locationGateway;
    private WeatherRecordGateway weatherGateway;
    private AuditRecordGateway auditGateway;
    private AuthorGateway authorGateway;

    @BeforeEach
    void setUp() {
        script = new EstimateFutureForecastScript();
        locationGateway = mock(LocationRowGateway.class);
        weatherGateway = mock(WeatherRecordGateway.class);
        auditGateway = mock(AuditRecordGateway.class);
        authorGateway = mock(AuthorGateway.class);
    }

    @Test
    void testExecute_ReturnsMostFrequentCondition() throws Exception {
       
        String locationName = "Lisbon";
        String authorName = "Alice";

        Location location = new Location(1, locationName);
        Author author = new Author(1, authorName);

        List<WeatherRecord> mockRecords = Arrays.asList(
                new WeatherRecord(location.getId(), LocalDate.now().minusDays(1), WeatherCondition.SUN, 20.0),
                new WeatherRecord(location.getId(), LocalDate.now().minusDays(2), WeatherCondition.SUN, 22.0),
                new WeatherRecord(location.getId(), LocalDate.now().minusDays(3), WeatherCondition.RAIN, 15.0)
        );

        when(locationGateway.getByName(locationName)).thenReturn(location);
        when(weatherGateway.getLastNHistoricalByLocation(location.getId(), 3)).thenReturn(mockRecords);
        when(authorGateway.findOrCreateByName(authorName)).thenReturn(author);

        
        String result = script.execute(locationGateway, weatherGateway, auditGateway, authorGateway, locationName, 3, authorName);

        assertEquals("The most likely weather condition for Lisbon is: Sun", result);
        verify(auditGateway).registerQuery(author.getId(), location.getId(), "ESTIMATE_FUTUTE_FORECAST");
    }

    @Test
    void testExecute_NoData_ReturnsMessage() throws Exception {
        when(locationGateway.getByName("Porto")).thenReturn(new Location(2, "Porto"));
        when(weatherGateway.getLastNHistoricalByLocation(anyInt(), anyInt())).thenReturn(List.of());
        when(authorGateway.findOrCreateByName(anyString())).thenReturn(new Author(1, "Tester"));

        String result = script.execute(locationGateway, weatherGateway, auditGateway, authorGateway, "Porto", 3, "Tester");

        assertTrue(result.contains("No sufficient data"));
    }

    @Test
    void testExecute_ThrowsApplicationException_WhenPersistenceFails() throws Exception {
        when(locationGateway.getByName(anyString())).thenThrow(new PersistenceException("DB error"));

        assertThrows(ApplicationException.class, () ->
                script.execute(locationGateway, weatherGateway, auditGateway, authorGateway, "Lisbon", 3, "Alice")
        );
    }
}
