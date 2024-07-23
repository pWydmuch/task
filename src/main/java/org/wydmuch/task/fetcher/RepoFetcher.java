package org.wydmuch.task.fetcher;

import org.springframework.stereotype.Service;
import org.wydmuch.task.fetcher.client.GithubClient;
import org.wydmuch.task.fetcher.response.BranchResponse;
import org.wydmuch.task.fetcher.client.Repo;
import org.wydmuch.task.fetcher.response.RepoResponse;

import java.util.List;

@Service
public class RepoFetcher {

    private final GithubClient githubClient;

    public RepoFetcher(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public List<RepoResponse> retrieveNonForReposForUser(String username) {
        return githubClient.getReposForUser(username).stream()
                .filter(repo -> !repo.isForked())
                .map(this::createRepoResponse)
                .toList();
    }

    private RepoResponse createRepoResponse(Repo repo) {
        List<BranchResponse> branches = githubClient.getBranchesOfRepo(repo.owner().login(), repo.name()).stream()
                .map(b -> new BranchResponse(b.name(), b.commit().sha()))
                .toList();
        return new RepoResponse(repo.name(), repo.owner().login(), branches);
    }
}
