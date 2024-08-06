package org.wydmuch.task;


import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WireMockTest(httpPort = 8081)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepoFetcherIT {

    private static final String URL_TEMPLATE = "/github/users/{username}/repos";
    private static final String EXISTING_USER = "pWydmuch";
    private static final String NON_EXISTING_USER = "I_DO_NOT_EXIST";
    private static final String UNSUPPORTED_MEDIA_MSG = "Invalid expected response format, JSON is the only one supported";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void givenUsernameOfExistingUserAndJsonAcceptHeaderReturnNonForkReposOfTheUser() {
        webTestClient.get()
                .uri(URL_TEMPLATE, EXISTING_USER)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(happyPathResponse());
    }

    @Test
    public void givenUsernameOfExistingUserAndNoAcceptHeaderReturnNonForkReposOfTheUser() {
        webTestClient.get()
                .uri(URL_TEMPLATE, EXISTING_USER)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(happyPathResponse());
    }

    @Test
    public void givenUsernameOfExistingUserAndXmlAcceptHeaderReturn406ErrorMessage() {
        webTestClient.get()
                .uri(URL_TEMPLATE, EXISTING_USER)
                .accept(MediaType.APPLICATION_XML)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE)
                .expectBody().json("""
                                       {
                                          "status": 406,
                                          "message": "%s"
                                       }
                        """.formatted(UNSUPPORTED_MEDIA_MSG));
    }

    @Test
    public void givenUsernameOFNonExistingUserAndJsonAcceptHeaderReturn404ErrorMessage() {
        webTestClient.get()
                .uri(URL_TEMPLATE, NON_EXISTING_USER)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("""
                                       {
                                          "status": 404,
                                          "message":  "User %s not found"
                                       }
                        """.formatted(NON_EXISTING_USER));
    }

    @Test
    public void givenUsernameOFNonExistingUserAndXmlAcceptHeaderReturn406ErrorMessage() {
        webTestClient.get()
                .uri(URL_TEMPLATE, NON_EXISTING_USER)
                .accept(MediaType.APPLICATION_XML)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE)
                .expectBody().json("""
                                       {
                                          "status": 406,
                                          "message": "%s"
                                       }
                        """.formatted(UNSUPPORTED_MEDIA_MSG));
    }

    @Test
    public void givenApiRateExceeded403ErrorMessage() {
        webTestClient.get()
                .uri(URL_TEMPLATE, "EXCEEDING_RATE_LIMITS")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody().json("""
                                       {
                                          "status": 403,
                                          "message": "Rate limit exceeded, try to provide token"
                                       }
                        """);
    }

    private static String happyPathResponse() {
        return """
                {
                  "owner_login": "%s",
                  "repos": [
                    {
                      "repo_name": "repoA",
                      "branches": [
                        {
                          "name": "1.0.A",
                          "last_commit_sha": "aa11"
                        },
                        {
                          "name": "2.0.A",
                          "last_commit_sha": "aa22"
                        },
                        {
                          "name": "3.0.A",
                          "last_commit_sha": "aa33"
                        }
                      ]
                    },
                    {
                      "repo_name": "repoB",
                      "branches": [
                        {
                          "name": "1.0.B",
                          "last_commit_sha": "bb11"
                        },
                        {
                          "name": "2.0.B",
                          "last_commit_sha": "bb22"
                        },
                        {
                          "name": "3.0.B",
                          "last_commit_sha": "bb33"
                        }
                      ]
                    }
                  ]
                }
                
                """.formatted(EXISTING_USER);
    }
}
