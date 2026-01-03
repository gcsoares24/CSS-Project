package pt.ul.fc.css.weatherwise;

import pt.ul.fc.css.weatherwise.presentation.AuditService;

import pt.ul.fc.css.weatherwise.presentation.ForecastService;
import pt.ul.fc.css.weatherwise.presentation.HistoryService;
import pt.ul.fc.css.weatherwise.business.exception.ApplicationException;
import pt.ul.fc.css.weatherwise.dataaccess.DataSource;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.dataaccess.rdgw.AuthorGateway;
import pt.ul.fc.css.weatherwise.entities.Author;
import pt.ul.fc.css.weatherwise.entities.WeatherCondition;
import pt.ul.fc.css.weatherwise.weatherwise.WeatherWise;

import java.sql.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SimpleClient {

    public static void main(String[] args) {
        SpringApplication.run(SimpleClient.class, args);
    }

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {
        	DataSource dataSource = DataSource.INSTANCE;

            try {
            	dataSource.connect();
                	
            } catch (PersistenceException e) {
                System.err.println("FATAL: Could not setup the database.");
                e.printStackTrace();
                return; // Exit if DB setup fails
            }

            WeatherWise app = new WeatherWise();
            try {
    			app.run();
    		} catch (ApplicationException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            System.out.println("--- WeatherWise Service Started Successfully ---");
            
            ForecastService forecastService = app.getForecastService();
            AuditService auditService = app.getAuditService();
            HistoryService historyService = app.getHistoryService();
            

            
            System.out.println("#### USE CASE A: Check current Coimbra forecast ####");
            try {
                String cityName = "Coimbra";
                String authorName = "Jao";
                // Get forecast
                String forecast = forecastService.getCurrentForecast(cityName, authorName);
                System.out.println(forecast);
            } catch (ApplicationException e) {
                System.out.println("Applidcation Error: " + e.getMessage());
            } catch (PersistenceException e) {
                System.out.println("Database Error: " + e.getMessage());
            } finally{
            	System.out.println("#### END OF USE CASE A: Check last Coimbra forecast ####");
            }

            System.out.println("#### USE CASE B: Update/Create historical forecast ####");
            try {
            	Date date = Date.valueOf("2027-11-09");
                String cityName = "Coimbra";
				WeatherCondition wc = WeatherCondition.fromId(5);
                Double temperature = 24.2;
                String authorName = "outro";
                
            	String response = forecastService.getUpdateForecastTS(cityName, date, wc, temperature, authorName);
            	System.out.println(response);
            } catch (ApplicationException e) {
                System.out.println("Applidcation Error: " + e.getMessage());
            } catch (PersistenceException e) {
                System.out.println("Database Error: " + e.getMessage());
            } finally{
                System.out.println("#### END OF USE CASE B ####");
            } 

             System.out.println("#### USE CASE C: Get audit history of queries ####");
             try {
                 String authorName = "Gui";
                 String response = auditService.getAllHistoryRecords(authorName);
            	 System.out.println(response);
            	 
             } catch (ApplicationException e) {
                 System.out.println("Application Error: " + e.getMessage());
             } catch (PersistenceException e) {
                 System.out.println("Database Error: " + e.getMessage());
             } finally{
                 System.out.println("#### END OF USE CASE C ####");
             }

             System.out.println("#### USE CASE D: Estimate future forecast ####");
             try {
                 String authorName = "Guizinho";
                 String cityName = "Coimbra";
                 int numberOfDays = 7;
                 // Get estimation
                 String estimation = historyService.getForecastEstimation(cityName, numberOfDays, authorName);
                 System.out.println(estimation);
             } catch (ApplicationException e) {
                 System.out.println("Application Error: " + e.getMessage());
             } catch (PersistenceException e) {
                 System.out.println("Database Error: " + e.getMessage());
             } finally{
            	 System.out.println("#### END OF USE CASE D ####");
             }
            
            
            
        
            dataSource.close();
            System.out.println("--- Database connection closed ---");
        };
    }
}
