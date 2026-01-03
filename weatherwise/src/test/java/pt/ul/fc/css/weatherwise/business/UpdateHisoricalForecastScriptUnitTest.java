package pt.ul.fc.css.weatherwise.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuditRecordGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuthorGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.LocationRowGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.WeatherRecordGateway;
import pt.ul.fc.css.weatherwise.entities.Author;
import pt.ul.fc.css.weatherwise.entities.Location;
import pt.ul.fc.css.weatherwise.entities.WeatherCondition;
import pt.ul.fc.css.weatherwise.entities.WeatherRecord;

class UpdateHisoricalForecastScriptUnitTest {

    private LocationRowGateway mockLocationGateway;
    private WeatherRecordGateway mockWeatherRecordGateway;
    private AuditRecordGateway mockAuditRecordGateway;
    private AuthorGateway mockAuthorGateway;

    private UpdateHistoricalForecastScript script;

    private static final String LOCATION_NAME = "Lisboa";
    private static final String AUTHOR_NAME = "TestUser";
    private static final int LOCATION_ID = 1;
    private static final int AUTHOR_ID = 99;
    private static final Date TEST_DATE = Date.valueOf(LocalDate.of(2025, 11, 10));
    private static final WeatherCondition TEST_CONDITION = WeatherCondition.SUN;
    private static final double TEST_TEMPERATURE = 22.5;

    @BeforeEach
    void setUp() throws PersistenceException {
        mockLocationGateway = mock(LocationRowGateway.class);
        mockWeatherRecordGateway = mock(WeatherRecordGateway.class);
        mockAuditRecordGateway = mock(AuditRecordGateway.class);
        mockAuthorGateway = mock(AuthorGateway.class);

        script = new UpdateHistoricalForecastScript();

        Location existingLocation = new Location(LOCATION_ID, LOCATION_NAME);
        Author existingAuthor = new Author(AUTHOR_ID, AUTHOR_NAME);

        when(mockLocationGateway.getByName(LOCATION_NAME)).thenReturn(existingLocation);
        when(mockAuthorGateway.findOrCreateByName(AUTHOR_NAME)).thenReturn(existingAuthor);
        
        when(mockWeatherRecordGateway.addForecast(anyInt(), any(Date.class), any(WeatherCondition.class), anyDouble()))
            .thenReturn(500);
    }
    
    @Test
    void testUpdateOrInsert_NewRecord_ExpectInsertAudit() throws ApplicationException, PersistenceException {
       
        when(mockWeatherRecordGateway.getWeatherByLocationAndDate(LOCATION_ID, TEST_DATE)).thenReturn(null);

        // Executar
        String result = script.updateOrInsertHistoricalForecast(
            mockLocationGateway, mockWeatherRecordGateway, mockAuditRecordGateway, mockAuthorGateway,
            LOCATION_NAME, TEST_DATE, TEST_CONDITION, TEST_TEMPERATURE, AUTHOR_NAME
        );

        assertTrue(result.contains("id=500 has been updated/inserted"), "Deve retornar mensagem de sucesso com o ID.");

        verify(mockWeatherRecordGateway).addForecast(LOCATION_ID, TEST_DATE, TEST_CONDITION, TEST_TEMPERATURE);

        verify(mockAuditRecordGateway).registerQuery(AUTHOR_ID, LOCATION_ID, "INSERT_FORECAST");
    }


    @Test
    void testUpdateOrInsert_ExistingRecord_ExpectUpdateAudit() throws ApplicationException, PersistenceException {
        WeatherRecord existingRecord = new WeatherRecord(LOCATION_ID, TEST_DATE.toLocalDate(), WeatherCondition.RAIN, 10.0);
        when(mockWeatherRecordGateway.getWeatherByLocationAndDate(LOCATION_ID, TEST_DATE)).thenReturn(existingRecord);

        String result = script.updateOrInsertHistoricalForecast(
            mockLocationGateway, mockWeatherRecordGateway, mockAuditRecordGateway, mockAuthorGateway,
            LOCATION_NAME, TEST_DATE, TEST_CONDITION, TEST_TEMPERATURE, AUTHOR_NAME
        );

        assertTrue(result.contains("id=500 has been updated/inserted"), "Deve retornar mensagem de sucesso com o ID.");

        verify(mockWeatherRecordGateway).addForecast(LOCATION_ID, TEST_DATE, TEST_CONDITION, TEST_TEMPERATURE);

        verify(mockAuditRecordGateway).registerQuery(AUTHOR_ID, LOCATION_ID, "UPDATE_FORECAST");
    }

  
    @Test
    void testUpdateOrInsert_InvalidLocation_ExpectApplicationException() throws PersistenceException {
        
        when(mockLocationGateway.getByName("Inexistente")).thenReturn(null);

        ApplicationException e = assertThrows(ApplicationException.class, () -> {
            script.updateOrInsertHistoricalForecast(
                mockLocationGateway, mockWeatherRecordGateway, mockAuditRecordGateway, mockAuthorGateway,
                "Inexistente", TEST_DATE, TEST_CONDITION, TEST_TEMPERATURE, AUTHOR_NAME
            );
        }, "Deveria lançar ApplicationException.");

        assertTrue(e.getMessage().contains("Location 'Inexistente' not found in database."), 
                   "A mensagem de erro deve refletir o problema da localização.");

        verify(mockWeatherRecordGateway, never()).addForecast(anyInt(), any(Date.class), any(WeatherCondition.class), anyDouble());
        verify(mockAuditRecordGateway, never()).registerQuery(anyInt(), anyInt(), anyString());
    }
}
