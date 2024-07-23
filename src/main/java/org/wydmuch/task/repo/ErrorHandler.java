package org.wydmuch.task.repo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.wydmuch.task.repo.response.ErrorMsgResponse;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorMsgResponse> handleBadAcceptMediaType() {
        HttpHeaders headers = createHeaderContentJson();
        ErrorMsgResponse errorMsg = new ErrorMsgResponse(HttpStatus.NOT_ACCEPTABLE.value(),
                "Invalid expected response format, JSON is the only one supported");
        return new ResponseEntity<>(errorMsg, headers, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMsgResponse> handleUserNotFoundException(UserNotFoundException ex) {
        HttpHeaders headers = createHeaderContentJson();
        ErrorMsgResponse errorMsg = new ErrorMsgResponse(HttpStatus.NOT_FOUND.value(),
                "User " + ex.getUsername() + " not found");
        return new ResponseEntity<>(errorMsg, headers, HttpStatus.NOT_FOUND);
    }

    private static HttpHeaders createHeaderContentJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}