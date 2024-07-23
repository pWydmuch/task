# Github Repo Fetcher

Retrieving repositories' information from github REST api

## Table of Contents

- [Technologies used](#technologies-used)

### Technologies used

 * Java 21
 * Spring Boot 3
 * JUnit5
 * WireMock
 * Gradle

### Spinning up the app

If you have Java 21 installed on your machine you can run the app with

```bash
 ./gradlew bootRun
 ```

If you don't have java, but you do have docker you can launch the app with

### Sidestepping rate limits

If you make several api calls in a short amount of time, you'll probably stumble upon problem resulting from rate limits imposed by Github,
in order to overcome this limitation you can provide authorization token. You can write it down directly in application.properties file, 
or export $GITHUB_TOKEN environmental variable e.g with the help of direnv

```
github.api.token=${GITHUB_TOKEN:}
```
To see how to generate your own token you can visit [this website](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-personal-access-token-classic)

### Testing

```bash
curl -H "Accept: application/json" http://localhost:8080/github/users/$USER_LOGIN/repos |  jq .
```

Alternatively, in case you don't have jq installed you can use python instead, the output would be a bit uglier though

```bash 
curl -H "Accept: application/json" http://localhost:8080/github/users/$USER_LOGIN/repos |  python -m json.tool
```

We can also check 

You can employ open api to that end as well, it accessible under link below

```
http://localhost:8080/swagger-ui/index.html
```


