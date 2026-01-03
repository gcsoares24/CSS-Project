package pt.ul.fc.css.weatherwise.business;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsultCurrentForecastScriptUnitTest {

    private ConsultCurrentForecastScript script;

    private LocationRowGateway mockLocationGateway;
    private WeatherRecordGateway mockWeatherRecordGateway;
    private AuditRecordGateway mockAuditRecordGateway;
    private AuthorGateway mockAuthorGateway;

    private static final String LOCATION_NAME = "Lisboa";
    private static final int LOCATION_ID = 1;
    private static final String AUTHOR_NAME = "TestUser";
    private static final int AUTHOR_ID = 99;
    private static final double TEMPERATURE = 25.5;
    private static final WeatherCondition CONDITION = WeatherCondition.SUN;

    @BeforeEach
    void setUp() {
        script = new ConsultCurrentForecastScript();
        mockLocationGateway = mock(LocationRowGateway.class);
        mockWeatherRecordGateway = mock(WeatherRecordGateway.class);
        mockAuditRecordGateway = mock(AuditRecordGateway.class);
        mockAuthorGateway = mock(AuthorGateway.class);
    }

  

    @Test
    void testGetCurrentForecast_Success() throws ApplicationException, PersistenceException {
        Location testLocation = new Location(LOCATION_ID, LOCATION_NAME);
        Author testAuthor = new Author(AUTHOR_ID, AUTHOR_NAME);
        WeatherRecord testRecord = new WeatherRecord(LOCATION_ID, null, CONDITION, TEMPERATURE);

        when(mockLocationGateway.getByName(LOCATION_NAME)).thenReturn(testLocation);
        when(mockWeatherRecordGateway.getLastByLocation(LOCATION_ID)).thenReturn(testRecord);
        when(mockAuthorGateway.findOrCreateByName(AUTHOR_NAME)).thenReturn(testAuthor);

        String result = script.getCurrentForecast(
                mockLocationGateway,
                mockWeatherRecordGateway,
                mockAuditRecordGateway,
                mockAuthorGateway,
                LOCATION_NAME,
                AUTHOR_NAME
        );

        String expected = String.format("The current forecast for %s is: %s, with a temperature of %.1f °C.",
                LOCATION_NAME, CONDITION.name(), TEMPERATURE);
        assertEquals(expected, result, "O resultado formatado deve corresponder à previsão mockada.");


        verify(mockAuditRecordGateway, times(1)).registerQuery(
                eq(AUTHOR_ID),
                eq(LOCATION_ID),
                eq("CURRENT_FORECAST")
        );
        
        verify(mockAuthorGateway, times(1)).findOrCreateByName(AUTHOR_NAME);
    }


    @Test
    void testGetCurrentForecast_InvalidLocation_ThrowsApplicationException() throws PersistenceException {
       
        when(mockLocationGateway.getByName(LOCATION_NAME)).thenReturn(null);

        
        ApplicationException e = assertThrows(ApplicationException.class, () -> {
            script.getCurrentForecast(
                    mockLocationGateway,
                    mockWeatherRecordGateway,
                    mockAuditRecordGateway,
                    mockAuthorGateway,
                    LOCATION_NAME,
                    AUTHOR_NAME
            );
        }, "Deveria lançar ApplicationException se a localização não for encontrada.");

        
        assertTrue(e.getMessage().contains("Location '" + LOCATION_NAME + "' not found in database."),
                "A mensagem de exceção deve estar correta.");

       
        verify(mockWeatherRecordGateway, never()).getLastByLocation(anyInt());
        verify(mockAuditRecordGateway, never()).registerQuery(anyInt(), anyInt(), anyString());
    }

  

    @Test
    void testGetCurrentForecast_NoCurrentRecord_ReturnsInformativeMessage() throws ApplicationException, PersistenceException {
     
        Location testLocation = new Location(LOCATION_ID, LOCATION_NAME);

    
        when(mockLocationGateway.getByName(LOCATION_NAME)).thenReturn(testLocation);
        when(mockWeatherRecordGateway.getLastByLocation(LOCATION_ID)).thenReturn(null);
        
       
        when(mockAuthorGateway.findOrCreateByName(AUTHOR_NAME)).thenReturn(new Author(AUTHOR_ID, AUTHOR_NAME));


       
        String result = script.getCurrentForecast(
                mockLocationGateway,
                mockWeatherRecordGateway,
                mockAuditRecordGateway,
                mockAuthorGateway,
                LOCATION_NAME,
                AUTHOR_NAME
        );

        String expectedMessage = "There is no forecast prevision for today (" + LOCATION_NAME + ").";
        assertEquals(expectedMessage, result, "Deveria retornar uma mensagem informativa.");

        
        verify(mockAuditRecordGateway, never()).registerQuery(anyInt(), anyInt(), anyString());
    }
}
