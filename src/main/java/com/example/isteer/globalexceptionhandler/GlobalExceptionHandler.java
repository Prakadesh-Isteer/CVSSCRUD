package com.example.isteer.globalexceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.isteer.dto.ErrorMessageDto;
import com.example.isteer.enums.CVSSEnum;
import com.example.isteer.exception.BussinessException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        logger.error("Validation error: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<String> handleDatabaseError(BadSqlGrammarException ex) {
        logger.error("Database error: {}", ex.getMessage());
        return new ResponseEntity<>("Database error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage());
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(BussinessException.class)
    public ResponseEntity<ErrorMessageDto> handleBusinessException(BussinessException ex) {
    	CVSSEnum error = ex.getError();
		logger.error("Business error: {}", ex.getMessage());
		ErrorMessageDto errorMessage = new ErrorMessageDto(error.getStatusCode(), error.getStatusMessage());
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
		logger.error("Null pointer error: {}", ex.getMessage());
		return new ResponseEntity<>("Null pointer error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
}
}
