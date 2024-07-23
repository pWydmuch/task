package org.wydmuch.task;

import org.springframework.stereotype.Service;
import org.wydmuch.task.model.Repo;

import java.util.List;

@Service
public class RepoFetcher {

    private final GithubClient githubClient;

    public RepoFetcher(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public List<Repo> retrieveReposForUser(String username) {
       return githubClient.getReposForUser(username).stream().filter(x -> !x.isForked()).toList();
   }
}
