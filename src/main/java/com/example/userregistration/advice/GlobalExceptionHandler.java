package com.example.userregistration.advice;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;

/**
 * @author NORUL
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * User-defined exception for business related exceptions
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NoResultException.class})
    @ResponseBody
    public ResponseEntity<String> handleNoResultException(NoResultException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
