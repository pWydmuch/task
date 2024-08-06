# Github Repo Fetcher

Retrieving repositories' information from github REST api

### Table of Contents

- [Technologies Used](#technologies-used)
- [Sidestepping Rate Limits](#sidestepping-rate-limits)
- [Spinning Up The App](#spinning-up-the-app)
- [Testing](#testing)
- [Api Documentation](#api-documentation)

### Technologies Used

 * Java 21
 * Spring Boot 3
 * JUnit 5
 * WireMock
 * Gradle
 * OpenApi
 * Docker

### Sidestepping Rate Limits

If you make several api calls in a short amount of time, you'll probably stumble upon problem resulting from rate limits imposed by Github,
in order to overcome this limitation you can provide authorization token. You can write it down directly in application.properties file,
or export $GITHUB_TOKEN environmental variable e.g with the help of [direnv](https://direnv.net/)

```
github.api.token=${GITHUB_TOKEN:}
```
To see how to generate your own token you can visit [this website](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-personal-access-token-classic)

### Spinning Up The App

If you have Java 21 installed on your machine you can run the app with

```bash
 ./gradlew bootRun
 ```

If you don't have java, but you do have docker you can launch the app by building an image 

```bash
docker build -t repo-fetcher .
```
And then running container based on the image

```bash
docker run -p 8080:8080 -e repo-fetcher
```
Or to have higher rate limits:

```bash
docker run -p 8080:8080 -e GITHUB_TOKEN=$GITHUB_TOKEN repo-fetcher
```

### Testing

#### Happy Path

```bash
curl -H "Accept: application/json" http://localhost:8080/github/users/$USER_LOGIN/repos |  jq .
```

With USER_LOGIN being pWydmuch, it should produce something along the lines of:

```json
{
  "owner_login": "pWydmuch",
  "repos": [
    {
      "repo_name": "chess",
      "branches": [
        {
          "name": "main",
          "last_commit_sha": "5d285c552d6bef8279e7582f7c4943f9a4d6071c"
        },
        {
          "name": "queen-diagonal",
          "last_commit_sha": "eb355fc6c05a6c4938706a9ee7138e79f9acf746"
        }
      ]
    },
    {
      "repo_name": "Converter",
      "branches": [
        {
          "name": "master",
          "last_commit_sha": "450ce449b36c17123e1b283a4c5b56090653db3a"
        }
      ]
    },
    {
      "repo_name": "docker-compose",
      "branches": [
        {
          "name": "master",
          "last_commit_sha": "28279bceb4580c973796547727512e9d590e1ff9"
        }
      ]
    },
    {
      "repo_name": "Minesweeper",
      "branches": [
        {
          "name": "master",
          "last_commit_sha": "8f513fc1a788b65452d344e5cdfb445ab9586a2e"
        },
        {
          "name": "maven",
          "last_commit_sha": "689180e9ad8d8f6cb24df3e3d4a58336fcf758e7"
        }
      ]
    }
  ]
}
```

Alternatively, in case you don't have jq installed you can use python instead, the output would be a bit uglier though

```bash 
curl -H "Accept: application/json" http://localhost:8080/github/users/$USER_LOGIN/repos |  python -m json.tool
```

#### User Not Found Case

```bash
curl -H "Accept: application/json" http://localhost:8080/github/users/I_DO_NOT_EXIST/repos |  jq .
```

```json
{
  "status": 404,
  "message": "User I_DO_NOT_EXIST not found"
}

```

#### Unsupported Accept Header Case

```bash
curl -H "Accept: application/xml" http://localhost:8080/github/users/$USER_LOGIN/repos |  jq .
```
```json
{
  "status": 406,
  "message": "Invalid expected response format, JSON is the only one supported"
}
```

#### Rate Limited Exceeded Case

You have to make this api call several times in order to cause rate limitation exception

```bash
curl -H "Accept: application/json" http://localhost:8080/github/users/$USER_LOGIN/repos |  jq .
```
```json
{
  "status": 403,
  "message": "Rate limit exceeded, try to provide token"
}
```
### Api Documentation

OpenApi documentation is accessible under the link below

```
http://localhost:8080/swagger-ui/index.html
```


