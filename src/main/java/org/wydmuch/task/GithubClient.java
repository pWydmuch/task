package org.wydmuch.task;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Set;

@HttpExchange
public interface GithubClient {
    @GetExchange("/users/{username}/repos")
    Set<Repo> getRepos(@PathVariable String username);
}