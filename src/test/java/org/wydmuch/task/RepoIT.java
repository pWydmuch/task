package org.wydmuch.task;


import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WireMockTest(httpPort = 8081)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RepoIT {

    private static final String USERNAME = "pWydmuch";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetUserById() throws Exception {
        mockMvc.perform(get("/github/" + USERNAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("ChessCodersCamp"))
                .andExpect(jsonPath("$[0].owner.login").value(USERNAME))
                .andExpect(jsonPath("$[1].owner.login").value(USERNAME));
    }
}
