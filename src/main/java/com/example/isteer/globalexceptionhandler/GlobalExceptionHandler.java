package com.example.isteer.globalexceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessageDto> handleValidationException(MethodArgumentNotValidException e) {
	    ErrorMessageDto errorDto = new ErrorMessageDto();
	    errorDto.setErrorCode(9321);

	    FieldError fieldError = e.getBindingResult().getFieldError();
	    String errorMessage = (fieldError != null)
	        ? fieldError.getDefaultMessage()
	        : "Validation error occurred";

	    errorDto.setErrorMessage(errorMessage);
	    logger.error("Validation error: {}", errorMessage);
	    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorMessageDto> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
	    logger.error("Data integrity violation: {}", ex.getMessage());
	    
	    // Check for duplicate key related to vulnerabilities
	    if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("vulnerabilities.dependency_uuid")) {
	        ErrorMessageDto errorMessage = new ErrorMessageDto(1001, 
	            "Duplicate CVE for this dependency already exists. Please use a different CVE or update the existing record.");
	        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
	    }

	    // Check for duplicate key related to applications
	    if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("applications.computer_uuid")) {
	        ErrorMessageDto errorMessage = new ErrorMessageDto(2000, 
	            "Application with same name already exists for this computer. Update failed");
	        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
	    }

	    if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("dependencies.application_uuid")) {
	        ErrorMessageDto errorMessage = new ErrorMessageDto(3000, 
	            "Dependency with same name already exists for this application. Update failed");
	        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
	    }
	    
	    if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("computers.ip_address")) {
	        return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorMessageDto(CVSSEnum.COMPUTER_WITH_SAME_IP_EXISTS.getStatusCode(), CVSSEnum.COMPUTER_WITH_SAME_IP_EXISTS.getStatusMessage())); 
	    }
	    
	    // Handle other data integrity violations
	    ErrorMessageDto errorMessage = new ErrorMessageDto(9999, "Data integrity error occurred.");
	    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}


}
