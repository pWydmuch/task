package org.wydmuch.task.fetcher.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RepoResponse(@JsonProperty("repo_name") String repoName,
                           @JsonProperty("owner_login") String ownerLogin,
                           @JsonProperty("branches") List<BranchResponse> branchResponses) {
}
