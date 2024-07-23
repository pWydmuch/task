package org.wydmuch.task.model;

import org.springframework.http.HttpStatus;

public record NotAcceptableErrorMsg(int status, String message) {
    public NotAcceptableErrorMsg() {
        this(HttpStatus.NOT_ACCEPTABLE.value(), "Unsupported media type, please use JSON instead");
   }
}
