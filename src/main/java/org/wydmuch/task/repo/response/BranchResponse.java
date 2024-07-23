package org.wydmuch.task.repo.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BranchResponse(String name, @JsonProperty("last_commit_sha") String lastCommitSha) {
}
