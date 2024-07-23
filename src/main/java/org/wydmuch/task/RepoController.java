package org.wydmuch.task;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.wydmuch.task.model.Repo;

import java.util.List;

@RestController
public class RepoController {

    private final RepoFetcher repoFetcher;

    public RepoController(RepoFetcher repoFetcher) {
        this.repoFetcher = repoFetcher;
    }

    @GetMapping(value = "/github/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Repo> github(@PathVariable String username) {
        return repoFetcher.retrieveReposForUser(username);
    }
}
