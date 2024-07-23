package org.wydmuch.task;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.wydmuch.task.model.NotAcceptableErrorMsg;
import org.wydmuch.task.model.Repo;

import java.util.List;

@RestController
public class RepoController {

    private final RepoFetcher repoFetcher;

    public RepoController(RepoFetcher repoFetcher) {
        this.repoFetcher = repoFetcher;
    }

    @GetMapping("/github/{username}")
    public List<Repo> github(@PathVariable String username) {
        return repoFetcher.retrieveReposForUser(username);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<NotAcceptableErrorMsg> handleUserNotFoundException() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new NotAcceptableErrorMsg(), headers, HttpStatus.NOT_ACCEPTABLE);
    }
}
