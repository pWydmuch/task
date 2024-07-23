package org.wydmuch.task.repo;

import org.springframework.stereotype.Service;
import org.wydmuch.task.repo.client.GithubClient;
import org.wydmuch.task.repo.response.BranchResponse;
import org.wydmuch.task.repo.client.Repo;
import org.wydmuch.task.repo.response.RepoResponse;

import java.util.List;

@Service
public class RepoFetcher {

    private final GithubClient githubClient;

    public RepoFetcher(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public List<RepoResponse> retrieveReposForUser(String username) {
       return githubClient.getReposForUser(username).stream()
               .filter(repo -> !repo.isForked())
               .map(this::createRepoResponse)
               .toList();
   }

    private RepoResponse createRepoResponse(Repo repo) {
        List<BranchResponse> branchesForUserInRepo = githubClient.getBranchesForUserInRepo(repo.owner().login(), repo.name()).stream()
                .map(b -> new BranchResponse(b.name(),b.commit().sha())).toList();
        return new RepoResponse(repo.name(), repo.owner().login(), branchesForUserInRepo);
    }
}
