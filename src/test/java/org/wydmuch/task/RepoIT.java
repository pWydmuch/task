package org.wydmuch.task;


import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Disabled;
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

    private static final String BASE_URL = "/github/";
    private static final String EXISTING_USER = "pWydmuch";
    private static final String NON_EXISTING_USER = "I_DO_NOT_EXIST";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenUsernameOfExistingUserAndJsonAcceptHeaderReturnReposOfTheUser() throws Exception {
        mockMvc.perform(get(BASE_URL + EXISTING_USER).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("chess"))
                .andExpect(jsonPath("$[0].owner.login").value(EXISTING_USER))
                .andExpect(jsonPath("$[1].owner.login").value(EXISTING_USER));
    }

    @Test
    public void givenUsernameOfExistingUserAndXmlAcceptHeaderReturn406ErrorMessage() throws Exception {
        mockMvc.perform(get(BASE_URL + EXISTING_USER).accept(MediaType.APPLICATION_XML))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_ACCEPTABLE.value()))
                .andExpect(jsonPath("$.message").value("Unsupported media type, please use JSON instead"));
    }

    @Test
    @Disabled
    public void givenUsernameOFNonExistingUserAndJsonAcceptHeaderReturn404ErrorMessage() throws Exception {
        mockMvc.perform(get(BASE_URL + NON_EXISTING_USER).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    @Disabled
    public void givenUsernameOFNonExistingUserAndXmlAcceptHeaderReturn406ErrorMessage() throws Exception {
        mockMvc.perform(get(BASE_URL + NON_EXISTING_USER).accept(MediaType.APPLICATION_XML))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_ACCEPTABLE.value()))
                .andExpect(jsonPath("$.message").value("Unsupported media type, please use JSON instead"));
    }
}
