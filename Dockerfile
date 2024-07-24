FROM eclipse-temurin:21 as build
COPY . .
CMD ["./gradlew", "bootJar"]

FROM eclipse-temurin:21
RUN mkdir app
WORKDIR app
COPY --from=build /build/libs/*.jar repo-fetcher.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "repo-fetcher.jar"]
