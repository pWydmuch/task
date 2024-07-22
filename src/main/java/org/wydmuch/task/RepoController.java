package org.wydmuch.task;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class RepoController {

    private final RepoFetcher repoFetcher;

    public RepoController(RepoFetcher repoFetcher) {
        this.repoFetcher = repoFetcher;
    }

    @GetMapping("/github/{username}")
    public Set<Repo> github(@PathVariable String username) {
        return repoFetcher.retrieveReposForUser(username);
    }
}
