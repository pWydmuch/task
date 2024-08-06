package org.wydmuch.task.fetcher;

import org.springframework.stereotype.Service;
import org.wydmuch.task.fetcher.client.GithubClient;
import org.wydmuch.task.fetcher.client.Repo;
import org.wydmuch.task.fetcher.response.BranchResponse;
import org.wydmuch.task.fetcher.response.RepoResponse;
import org.wydmuch.task.fetcher.response.ReposWithOwnerResponse;
import reactor.core.publisher.Mono;

@Service
public class RepoFetcher {

    private final GithubClient githubClient;

    public RepoFetcher(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public Mono<ReposWithOwnerResponse> retrieveNonForReposForUser(String username) {
        return githubClient.getReposForUser(username)
                .filter(x -> !x.isForked())
                .flatMap(this::createRepoResponse)
                .collectList()
                .map(repoResponses -> new ReposWithOwnerResponse(username, repoResponses));
    }

    private Mono<RepoResponse> createRepoResponse(Repo repo) {
        return githubClient.getBranchesOfRepo(repo.owner().login(), repo.name())
                .map(b -> new BranchResponse(b.name(), b.commit().sha()))
                .collectList()
                .map(branches -> new RepoResponse(repo.name(), branches));
    }
}