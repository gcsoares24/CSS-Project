package pt.ul.fc.css.weatherwise.presentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.ul.fc.css.weatherwise.business.EstimateFutureForecastScript;
import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuditRecordGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuthorGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.LocationRowGateway;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.WeatherRecordGateway;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceUnitTest {

    private HistoryService historyService;

    @Mock
    private EstimateFutureForecastScript mockEstimateForecastTS;

    private static final String TEST_LOCATION = "Coimbra";
    private static final int TEST_DAYS = 10;
    private static final String TEST_AUTHOR = "Testador";

    @BeforeEach
    void setUp() {
        historyService = new HistoryService(mockEstimateForecastTS);
    }

   
    @Test
    void testGetForecastEstimation_Success() throws ApplicationException, PersistenceException {
        
        String expectedResult = "The most likely weather condition for Coimbra is: Rain";
        
       
        when(mockEstimateForecastTS.execute(
                any(LocationRowGateway.class),
                any(WeatherRecordGateway.class),
                any(AuditRecordGateway.class),
                any(AuthorGateway.class),
                eq(TEST_LOCATION),
                eq(TEST_DAYS),
                eq(TEST_AUTHOR)
        )).thenReturn(expectedResult);

       
        String actualResult = historyService.getForecastEstimation(TEST_LOCATION, TEST_DAYS, TEST_AUTHOR);

        assertEquals(expectedResult, actualResult, "O resultado deve ser o retornado pelo script.");
        
        
        verify(mockEstimateForecastTS, times(1)).execute(
                any(LocationRowGateway.class),
                any(WeatherRecordGateway.class),
                any(AuditRecordGateway.class),
                any(AuthorGateway.class),
                eq(TEST_LOCATION),
                eq(TEST_DAYS),
                eq(TEST_AUTHOR)
        );
    }

   
    @Test
    void testGetForecastEstimation_ApplicationExceptionPropagation() throws ApplicationException, PersistenceException {
       
        String exceptionMessage = "Invalid Location: Location 'Neverland' not found.";
        
       
        doThrow(new ApplicationException(exceptionMessage))
            .when(mockEstimateForecastTS).execute(any(), any(), any(), any(), eq(TEST_LOCATION), eq(TEST_DAYS), eq(TEST_AUTHOR));

        
        ApplicationException thrown = assertThrows(ApplicationException.class, () -> {
            historyService.getForecastEstimation(TEST_LOCATION, TEST_DAYS, TEST_AUTHOR);
        }, "Deveria lançar ApplicationException.");
        
        assertEquals(exceptionMessage, thrown.getMessage());
    }

   
    @Test
    void testGetForecastEstimation_PersistenceExceptionPropagation() throws ApplicationException, PersistenceException {
       
        String exceptionMessage = "Database connection failed.";
        
        
        doThrow(new PersistenceException(exceptionMessage))
            .when(mockEstimateForecastTS).execute(any(), any(), any(), any(), eq(TEST_LOCATION), eq(TEST_DAYS), eq(TEST_AUTHOR));

        
        PersistenceException thrown = assertThrows(PersistenceException.class, () -> {
            historyService.getForecastEstimation(TEST_LOCATION, TEST_DAYS, TEST_AUTHOR);
        }, "Deveria lançar PersistenceException.");
        
        assertEquals(exceptionMessage, thrown.getMessage());
    }

}
