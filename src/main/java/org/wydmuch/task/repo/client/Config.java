package org.wydmuch.task.repo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.wydmuch.task.repo.UserNotFoundException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class Config {
    private static final Pattern USERNAME_EXTRACTOR = Pattern.compile("/users/([^/]+)/");

    @Bean
    public GithubClient githubClient(@Value("${github.api.baseurl}") String baseUrl) {
        RestClient restClient = RestClient.builder()
                .defaultStatusHandler(x -> x.isSameCodeAs(HttpStatus.NOT_FOUND), (req, res) -> {
                    Matcher matcher = USERNAME_EXTRACTOR.matcher(req.getURI().toString());
                    if (matcher.find()) {
                        throw new UserNotFoundException(matcher.group(1));
                    }
                })
                .baseUrl(baseUrl)
                .build();
        RestClientAdapter exchangeAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(exchangeAdapter).build();
        return factory.createClient(GithubClient.class);
    }

}