package org.wydmuch.task.repo.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface GithubClient {
    @GetExchange("/users/{username}/repos")
    List<Repo> getReposForUser(@PathVariable String username);

    @GetExchange("/repos/{username}/{repo}/branches")
    List<Branch> getBranchesInRepo(@PathVariable String username, @PathVariable String repo);
}