package com.nookure.staff.api.jenkins.json;

import org.jetbrains.annotations.NotNull;

public record JenkinsArtifact(
        @NotNull String displayPath,
        @NotNull String fileName,
        @NotNull String relativePath) {}
