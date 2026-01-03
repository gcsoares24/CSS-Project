package pt.ul.fc.css.weatherwise.presentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.ul.fc.css.weatherwise.business.ConsultCurrentForecastScript;
import pt.ul.fc.css.weatherwise.business.UpdateHistoricalForecastScript;
import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuditRecordGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuthorGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.LocationRowGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.WeatherRecordGateway;
import pt.ul.fc.css.weatherwise.entities.WeatherCondition;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForecastServiceUnitTest {

    private ForecastService forecastService;

    @Mock
    private ConsultCurrentForecastScript mockConsultForecastTS;
    @Mock
    private UpdateHistoricalForecastScript mockUpdateForecastTS;

    private static final String TEST_LOCATION = "Lisboa";
    private static final String TEST_AUTHOR = "Testador";

    @BeforeEach
    void setUp() {
        forecastService = new ForecastService(mockConsultForecastTS, mockUpdateForecastTS);
    }


    @Test
    void testGetCurrentForecast_Success() throws ApplicationException, PersistenceException {

        String expectedResult = "The current forecast for Lisboa is: SUN, with a temperature of 25.0 °C.";
        
        when(mockConsultForecastTS.getCurrentForecast(
                any(LocationRowGateway.class),
                any(WeatherRecordGateway.class),
                any(AuditRecordGateway.class),
                any(AuthorGateway.class),
                eq(TEST_LOCATION),
                eq(TEST_AUTHOR)
        )).thenReturn(expectedResult);

        String actualResult = forecastService.getCurrentForecast(TEST_LOCATION, TEST_AUTHOR);

        assertEquals(expectedResult, actualResult, "O resultado deve ser o retornado pelo script.");
        
        verify(mockConsultForecastTS, times(1)).getCurrentForecast(
                any(LocationRowGateway.class),
                any(WeatherRecordGateway.class),
                any(AuditRecordGateway.class),
                any(AuthorGateway.class),
                eq(TEST_LOCATION),
                eq(TEST_AUTHOR)
        );
    }

    void testGetCurrentForecast_ApplicationExceptionPropagation() throws ApplicationException, PersistenceException {
        
        String exceptionMessage = "Invalid Location: Location 'Neverland' not found.";
        
        
        doThrow(new ApplicationException(exceptionMessage))
            .when(mockConsultForecastTS).getCurrentForecast(any(), any(), any(), any(), eq(TEST_LOCATION), eq(TEST_AUTHOR));

        
        ApplicationException thrown = assertThrows(ApplicationException.class, () -> {
            forecastService.getCurrentForecast(TEST_LOCATION, TEST_AUTHOR);
        }, "Deveria lançar ApplicationException.");
        
        assertEquals(exceptionMessage, thrown.getMessage());
    }
    
    
    @Test
    void testGetUpdateForecastTS_Success() throws ApplicationException, PersistenceException {
        
        Date testDate = Date.valueOf(LocalDate.of(2025, 12, 10));
        WeatherCondition testCondition = WeatherCondition.RAIN;
        Double testTemperature = 15.5;
        String expectedResult = "The DailyForecast with id=100 has been updated/inserted";
        
       
        when(mockUpdateForecastTS.updateOrInsertHistoricalForecast(
                any(LocationRowGateway.class),
                any(WeatherRecordGateway.class),
                any(AuditRecordGateway.class),
                any(AuthorGateway.class),
                eq(TEST_LOCATION),
                eq(testDate),
                eq(testCondition),
                eq(testTemperature),
                eq(TEST_AUTHOR)
        )).thenReturn(expectedResult);

        
        String actualResult = forecastService.getUpdateForecastTS(
                TEST_LOCATION, testDate, testCondition, testTemperature, TEST_AUTHOR
        );

        
        assertEquals(expectedResult, actualResult, "O resultado deve ser o retornado pelo script.");
        
        
        verify(mockUpdateForecastTS, times(1)).updateOrInsertHistoricalForecast(
                any(LocationRowGateway.class),
                any(WeatherRecordGateway.class),
                any(AuditRecordGateway.class),
                any(AuthorGateway.class),
                eq(TEST_LOCATION),
                eq(testDate),
                eq(testCondition),
                eq(testTemperature),
                eq(TEST_AUTHOR)
        );
    }
    

    @Test
    void testGetUpdateForecastTS_PersistenceExceptionPropagation() throws ApplicationException, PersistenceException {
        
        Date testDate = Date.valueOf(LocalDate.of(2025, 12, 10));
        WeatherCondition testCondition = WeatherCondition.SNOW;
        Double testTemperature = -2.0;
        String exceptionMessage = "DB connection failure.";
        
        doThrow(new PersistenceException(exceptionMessage))
            .when(mockUpdateForecastTS).updateOrInsertHistoricalForecast(
                    any(), any(), any(), any(), eq(TEST_LOCATION), eq(testDate), eq(testCondition), eq(testTemperature), eq(TEST_AUTHOR));

        PersistenceException thrown = assertThrows(PersistenceException.class, () -> {
            forecastService.getUpdateForecastTS(TEST_LOCATION, testDate, testCondition, testTemperature, TEST_AUTHOR);
        }, "Deveria lançar PersistenceException.");
        
        assertEquals(exceptionMessage, thrown.getMessage());
    }

}
