package org.wydmuch.task.fetcher;

public class UserNotFoundException extends RuntimeException {
    private final String username;

    public UserNotFoundException(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
