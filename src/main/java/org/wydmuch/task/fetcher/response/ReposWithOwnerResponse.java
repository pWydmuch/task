package org.wydmuch.task.fetcher.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ReposWithOwnerResponse(@JsonProperty("owner_login") String ownerLogin,
                                     List<RepoResponse> repos){
}
