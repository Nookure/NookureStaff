package com.nookure.staff.api.jenkins.json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record JenkinsRun(
        @NotNull String _class,
        boolean building,
        @Nullable String description,
        long duration,
        @NotNull String fullDisplayName,
        @NotNull String url,
        @NotNull JenkinsArtifact[] artifacts) {}
