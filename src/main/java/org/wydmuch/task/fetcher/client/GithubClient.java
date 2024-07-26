package org.wydmuch.task.fetcher.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Flux;

@HttpExchange
public interface GithubClient {
    @GetExchange("/users/{username}/repos")
    Flux<Repo> getReposForUser(@PathVariable String username);

    @GetExchange("/repos/{username}/{repo}/branches")
    Flux<Branch> getBranchesOfRepo(@PathVariable String username, @PathVariable String repo);
}