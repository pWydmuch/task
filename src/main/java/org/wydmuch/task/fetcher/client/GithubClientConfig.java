package org.wydmuch.task.fetcher.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.wydmuch.task.fetcher.errorhandling.ErrorHandler;

@Configuration
public class GithubClientConfig {

    @Bean
    public GithubClient githubClient(@Value("${github.api.baseurl}") String baseUrl,
                                     @Value("${github.api.token}") String token) {
        WebClient webClient = createWebClient(baseUrl, token);
        WebClientAdapter exchangeAdapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(exchangeAdapter).build();
        return factory.createClient(GithubClient.class);
    }

    private static WebClient createWebClient(String baseUrl, String token) {
        return WebClient.builder()
                .filter(ErrorHandler.handleErrorsFilter())
                .defaultHeaders(h -> addAuthorizationHeaderIfTokenPresent(token, h))
                .baseUrl(baseUrl)
                .build();
    }

    private static void addAuthorizationHeaderIfTokenPresent(String token, HttpHeaders h) {
        if (token != null && !token.isBlank()) {
            h.add("Authorization", "Bearer " + token);
        }
    }

}
