package org.wydmuch.task.fetcher;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.wydmuch.task.fetcher.response.ErrorMsgResponse;
import org.wydmuch.task.fetcher.response.RepoResponse;

import java.util.List;

@RestController
public class RepoFetcherController {

    private final RepoFetcher repoFetcher;

    public RepoFetcherController(RepoFetcher repoFetcher) {
        this.repoFetcher = repoFetcher;
    }

    @Operation(description = "Fetches info of non fork repos for a given user", summary = "Get user's repos",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "User not found", responseCode = "404", content = @Content(
                            schema = @Schema(implementation = ErrorMsgResponse.class), examples = @ExampleObject("""
                            {
                                "status": 404,
                                "message": "User $USERNAME not found"
                            }
                            """
                    ))),
                    @ApiResponse(description = "Not acceptable media type format", responseCode = "406", content = @Content(
                            schema = @Schema(implementation = ErrorMsgResponse.class), examples = @ExampleObject("""
                            {
                                "status": 406,
                                "message": "Invalid expected response format, JSON is the only one supported""
                            }
                            """
                    )))
            })
    @GetMapping(value = "/github/users/{username}/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RepoResponse> retrieveNonForkRepos(@PathVariable String username) {
        return repoFetcher.retrieveNonForReposForUser(username);
    }
}