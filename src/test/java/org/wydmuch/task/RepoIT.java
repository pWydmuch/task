package org.wydmuch.task;


import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WireMockTest(httpPort = 8081)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RepoIT {

    private static final String URL_TEMPLATE = "/github/users/{username}/repos";
    private static final String EXISTING_USER = "pWydmuch";
    private static final String NON_EXISTING_USER = "I_DO_NOT_EXIST";
    private static final String UNSUPPORTED_MEDIA_MSG = "Invalid expected response format, JSON is the only one supported";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenUsernameOfExistingUserAndJsonAcceptHeaderReturnNonForkReposOfTheUser() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE, EXISTING_USER).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].repo_name").value("repoA"))
                .andExpect(jsonPath("$[1].repo_name").value("repoB"))
                .andExpect(jsonPath("$[0].owner_login").value(EXISTING_USER))
                .andExpect(jsonPath("$[1].owner_login").value(EXISTING_USER))
                .andExpect(jsonPath("$[0].branches[0].name").value("1.0.A"))
                .andExpect(jsonPath("$[0].branches[0].last_commit_sha").value("aa11"))
                .andExpect(jsonPath("$[1].branches[2].name").value("3.0.B"))
                .andExpect(jsonPath("$[1].branches[2].last_commit_sha").value("bb33"));
    }

    @Test
    public void givenUsernameOfExistingUserAndXmlAcceptHeaderReturn406ErrorMessage() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE, EXISTING_USER).accept(MediaType.APPLICATION_XML))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_ACCEPTABLE.value()))
                .andExpect(jsonPath("$.message").value(UNSUPPORTED_MEDIA_MSG));
    }

    @Test
    public void givenUsernameOFNonExistingUserAndJsonAcceptHeaderReturn404ErrorMessage() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE, NON_EXISTING_USER).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("User " + NON_EXISTING_USER + " not found"));
    }

    @Test
    public void givenUsernameOFNonExistingUserAndXmlAcceptHeaderReturn406ErrorMessage() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE, NON_EXISTING_USER).accept(MediaType.APPLICATION_XML))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_ACCEPTABLE.value()))
                .andExpect(jsonPath("$.message").value(UNSUPPORTED_MEDIA_MSG));
    }
}
