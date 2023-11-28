package com.EmployeeManagement.handler;

import com.EmployeeManagement.dto.ErrorResponse;
import com.EmployeeManagement.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> getEntity(NotFoundException ex){
        int status= HttpStatus.NOT_FOUND.value();
        return ResponseEntity.status(status)
                .body(ErrorResponse.builder()
                        .status(status)
                        .message(ex.getMessage())
                        .build());
    }
}
