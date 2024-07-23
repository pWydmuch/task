package org.wydmuch.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Repo(String name, Owner owner, @JsonProperty("fork") boolean isForked) {}