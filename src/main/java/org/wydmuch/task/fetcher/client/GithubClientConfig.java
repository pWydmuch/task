package org.wydmuch.task.fetcher.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.wydmuch.task.fetcher.errorhandling.RateLimitExceededException;
import org.wydmuch.task.fetcher.errorhandling.UserNotFoundException;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class GithubClientConfig {
    private static final Pattern USERNAME_EXTRACTOR = Pattern.compile("/users/([^/]+)/");

    @Bean
    public GithubClient githubClient(@Value("${github.api.baseurl}") String baseUrl,
                                     @Value("${github.api.token}") String token) {
        WebClient webClient = createRestClient(baseUrl, token);
        WebClientAdapter exchangeAdapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(exchangeAdapter).build();
        return factory.createClient(GithubClient.class);
    }

    private static WebClient createRestClient(String baseUrl, String token) {
        return WebClient.builder()
                .defaultStatusHandler(x -> x.isSameCodeAs(HttpStatus.NOT_FOUND),
                        GithubClientConfig::throwExceptionIfUserNotFound)
                .defaultStatusHandler(x -> x.isSameCodeAs(HttpStatus.FORBIDDEN),
                        __ -> {
                            throw new RateLimitExceededException();
                        })
                .defaultHeaders(h -> addAuthorizationHeaderIfTokenNotNull(token, h))
                .baseUrl(baseUrl)
                .build();
    }


    private static Mono<? extends Throwable> throwExceptionIfUserNotFound(ClientResponse res) {
        Matcher matcher = USERNAME_EXTRACTOR.matcher(res.request().getURI().toString());
        return  matcher.find() ? Mono.error(new UserNotFoundException(matcher.group(1))) : Mono.empty();
    }

    private static void addAuthorizationHeaderIfTokenNotNull(String token, HttpHeaders h) {
        if (token != null && !token.isBlank()) {
            h.add("Authorization", "Bearer " + token);
        }
    }

}
