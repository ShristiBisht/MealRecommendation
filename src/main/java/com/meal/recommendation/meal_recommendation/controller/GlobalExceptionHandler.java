package com.meal.recommendation.meal_recommendation.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        // Handle exceptions and show a friendly page
        return "error";  // You can create an error.html page in the templates folder
    }
}
