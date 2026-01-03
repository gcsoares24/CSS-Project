package pt.ul.fc.css.urbanwheels.exceptions;

import org.springframework.ui.Model;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import pt.ul.fc.css.urbanwheels.controller.BikeWebController;

import pt.ul.fc.css.urbanwheels.controller.StationWebController;

import pt.ul.fc.css.urbanwheels.controller.UserWebController;
import pt.ul.fc.css.urbanwheels.controller.WebController;

import java.time.LocalDateTime;

@ControllerAdvice(assignableTypes = {
	    BikeWebController.class, 
	    StationWebController.class, 
	    UserWebController.class,
	    WebController.class
	})public class ThymeleafExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("timestamp", LocalDateTime.now());
        return "error";
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleBadRequest(IllegalArgumentException ex, Model model) {
    	model.addAttribute("status", 400);
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("timestamp", LocalDateTime.now());
        return "error";
    }

}
