package com.assignment.common.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleServiceException(final ApiException e) {
        ErrorResponse err = ErrorResponse.builder()
                .message(e.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();

        return err.toEntity();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleServiceException(final MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for(var fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]\n");
        }

        ErrorResponse<Map<String, Object>> err = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), builder.toString());
        Map<String, Object> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            Map<String, Object> fieldErrorDetails = new HashMap<>();
            fieldErrorDetails.put("defaultMessage", fieldError.getDefaultMessage());
            fieldErrorDetails.put("isError", true);
            errorMap.put(fieldError.getField(), fieldErrorDetails);
        });

        err.setData(errorMap);
        return err.toEntity();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder builder = new StringBuilder();
        e.getConstraintViolations().forEach(violation -> {
            builder.append("[");
            builder.append(violation.getPropertyPath());
            builder.append("](은)는 ");
            builder.append(violation.getMessage());
            builder.append(" 입력된 값: [");
            builder.append(violation.getInvalidValue());
            builder.append("]\n");
        });

        ErrorResponse<Map<String, Object>> err = new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), builder.toString());
        Map<String, Object> errorMap = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            Map<String, Object> fieldErrorDetails = new HashMap<>();
            fieldErrorDetails.put("defaultMessage", violation.getMessage());
            fieldErrorDetails.put("isError", true);
            errorMap.put(violation.getPropertyPath().toString(), fieldErrorDetails);
        });

        err.setData(errorMap);
        return err.toEntity();
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleTokenExpiredException(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is expired");
    }
}
