package org.wydmuch.task;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.wydmuch.task.model.ErrorMsg;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorMsg> handleBadAcceptMediaType() {
        HttpHeaders headers = createHeaderContentJson();
        ErrorMsg errorMsg = new ErrorMsg(HttpStatus.NOT_ACCEPTABLE.value(),
                "Unsupported media type, please use JSON instead");
        return new ResponseEntity<>(errorMsg, headers, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMsg> handleUserNotFoundException(UserNotFoundException ex) {
        HttpHeaders headers = createHeaderContentJson();
        ErrorMsg errorMsg = new ErrorMsg(HttpStatus.NOT_FOUND.value(),
                "User " + ex.getUsername() + " not found");
        return new ResponseEntity<>(errorMsg, headers, HttpStatus.NOT_FOUND);
    }

    private static HttpHeaders createHeaderContentJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
