package com.isteer.exceptions.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.isteer.dto.ErrorMessageDto;
import com.isteer.enums.CVSSEnum;
import com.isteer.exception.BussinessException;
import com.isteer.util.StatusMessageUtil;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageDto> handleIllegalArgument(IllegalArgumentException ex) {
        logger.error("Validation error: {}", ex.getMessage());
        ErrorMessageDto errorMessage = new ErrorMessageDto(CVSSEnum.ILLEGAL_ARGUMENT.getStatusCode(), String.format("%s %s", StatusMessageUtil.getMessage(CVSSEnum.ILLEGAL_ARGUMENT), ex.getMessage()));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<ErrorMessageDto> handleDatabaseError(BadSqlGrammarException ex) {
        logger.error("Database error: {}", ex.getMessage());
        ErrorMessageDto errorMessage = new ErrorMessageDto(CVSSEnum.INVALID_SQL_SYNTAX.getStatusCode(),String.format("%s %s", StatusMessageUtil.getMessage(CVSSEnum.INVALID_SQL_SYNTAX), ex.getMessage()));
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDto> handleGeneralException(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage());
        ErrorMessageDto errorMessage = new ErrorMessageDto(CVSSEnum.Internal_Server_Error.getStatusCode(), String.format("%s %s", StatusMessageUtil.getMessage(CVSSEnum.Internal_Server_Error), ex.getMessage()));
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(BussinessException.class)
    public ResponseEntity<ErrorMessageDto> handleBusinessException(BussinessException ex) {
        CVSSEnum error = ex.getError();
        logger.error("Business error: {}", ex.getMessage());
        ErrorMessageDto errorMessage = new ErrorMessageDto(error.getStatusCode(), StatusMessageUtil.getMessage(error));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorMessageDto> handleNullPointerException(NullPointerException ex) {
        logger.error("Null pointer error: {}", ex.getMessage());
        ErrorMessageDto errorMessage = new ErrorMessageDto(CVSSEnum.NULL_POINTER_EXCEPTION.getStatusCode(),String.format("%s %s", StatusMessageUtil.getMessage(CVSSEnum.NULL_POINTER_EXCEPTION), ex.getMessage()));
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessageDto> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        logger.error("JSON parse error: {}", ex.getMessage());
        
        ErrorMessageDto errorResponse = new ErrorMessageDto(CVSSEnum.INVALID_INPUT.getStatusCode(), 
				StatusMessageUtil.getMessage(CVSSEnum.INVALID_INPUT));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDto> handleValidationException(MethodArgumentNotValidException e) {
        ErrorMessageDto errorDto = new ErrorMessageDto();
        errorDto.setErrorCode(9321);

        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMessage = (fieldError != null)
            ? fieldError.getDefaultMessage()
            : CVSSEnum.VALIDATION_ERROR.getMessageKey();

        errorDto.setErrorMessage(errorMessage);
        logger.error("Validation error: {}", errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessageDto> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        logger.error("Data integrity violation: {}", ex.getMessage());
        
        // Check for duplicate key related to vulnerabilities
        if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("vulnerabilities.dependency_uuid")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(new ErrorMessageDto(CVSSEnum.VULNERABILITY_ALREADY_EXISTS.getStatusCode(), StatusMessageUtil.getMessage(CVSSEnum.VULNERABILITY_ALREADY_EXISTS)));
        }

        // Check for duplicate key related to applications
        if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("applications.computer_uuid")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(new ErrorMessageDto(CVSSEnum.APPLICATION_WITH_SAME_NAME_EXISTS.getStatusCode(), StatusMessageUtil.getMessage(CVSSEnum.APPLICATION_WITH_SAME_NAME_EXISTS)));
        }

        if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("dependencies.application_uuid")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(new ErrorMessageDto(CVSSEnum.DEPENDENCY_WITH_SAME_NAME_EXISTS.getStatusCode(), StatusMessageUtil.getMessage(CVSSEnum.DEPENDENCY_WITH_SAME_NAME_EXISTS))); 
        }
        
        if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("computers.ip_address")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorMessageDto(CVSSEnum.COMPUTER_WITH_SAME_IP_EXISTS.getStatusCode(),StatusMessageUtil.getMessage(CVSSEnum.COMPUTER_WITH_SAME_IP_EXISTS))); 
        }
        
        // Handle other data integrity violations
        ErrorMessageDto errorMessage = new ErrorMessageDto(CVSSEnum.DATA_INTEGRITY_VIOLATION.getStatusCode(),String.format("%s %s", StatusMessageUtil.getMessage(CVSSEnum.DATA_INTEGRITY_VIOLATION), ex.getMessage()));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}