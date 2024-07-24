package org.wydmuch.task.fetcher.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.wydmuch.task.fetcher.errorhandling.RateLimitExceededException;
import org.wydmuch.task.fetcher.errorhandling.UserNotFoundException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class GithubClientConfig {
    private static final Pattern USERNAME_EXTRACTOR = Pattern.compile("/users/([^/]+)/");

    @Bean
    public GithubClient githubClient(@Value("${github.api.baseurl}") String baseUrl,
                                     @Value("${github.api.token}") String token) {
        RestClient restClient = RestClient.builder()
                .defaultStatusHandler(x -> x.isSameCodeAs(HttpStatus.NOT_FOUND),
                        (req, res) -> throwExceptionIfUserNotFound(req))
                .defaultStatusHandler(x -> x.isSameCodeAs(HttpStatus.FORBIDDEN),
                        (req, res) -> {
                            throw new RateLimitExceededException();
                        })
                .defaultHeaders(h -> addAuthorizationHeaderIfTokenNotNull(token, h))
                .baseUrl(baseUrl)
                .build();
        RestClientAdapter exchangeAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(exchangeAdapter).build();
        return factory.createClient(GithubClient.class);
    }


    private static void throwExceptionIfUserNotFound(HttpRequest req) {
        Matcher matcher = USERNAME_EXTRACTOR.matcher(req.getURI().toString());
        if (matcher.find()) {
            throw new UserNotFoundException(matcher.group(1));
        }
    }

    private static void addAuthorizationHeaderIfTokenNotNull(String token, HttpHeaders h) {
        if (token != null && !token.isBlank()) {
            h.add("Authorization", "Bearer " + token);
        }
    }

}
