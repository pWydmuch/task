package org.wydmuch.task;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.wydmuch.task.model.Repo;

import java.util.List;

@HttpExchange
public interface GithubClient {
    @GetExchange("/users/{username}/repos")
    List<Repo> getRepos(@PathVariable String username);
}