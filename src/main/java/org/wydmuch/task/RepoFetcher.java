package org.wydmuch.task;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RepoFetcher {

    private final GithubClient githubClient;

    public RepoFetcher(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public Set<Repo> retrieveReposForUser(String username) {
        return githubClient.getRepos(username);
    }
}
