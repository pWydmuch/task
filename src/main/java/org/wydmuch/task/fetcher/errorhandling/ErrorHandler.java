package org.wydmuch.task.fetcher.errorhandling;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.wydmuch.task.fetcher.response.ErrorMsgResponse;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class ErrorHandler {

    private static final Pattern USERNAME_EXTRACTOR = Pattern.compile("/users/([^/]+)/");

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

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorMsgResponse> handleRateLimitExceededException() {
        HttpHeaders headers = createHeaderContentJson();
        ErrorMsgResponse errorMsg = new ErrorMsgResponse(HttpStatus.FORBIDDEN.value(),
                "Rate limit exceeded, try to provide token");
        return new ResponseEntity<>(errorMsg, headers, HttpStatus.FORBIDDEN);
    }

    public static ExchangeFilterFunction handleErrorsFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(res -> {
            if (res.statusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                Matcher matcher = USERNAME_EXTRACTOR.matcher(res.request().getURI().toString());
                return  matcher.find() ? Mono.error(new UserNotFoundException(matcher.group(1))) : Mono.empty();
           } else if (res.statusCode().isSameCodeAs(HttpStatus.FORBIDDEN)) {
                return Mono.error(new RateLimitExceededException());
            }
            return Mono.just(res);
      });
    }

    private static HttpHeaders createHeaderContentJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
