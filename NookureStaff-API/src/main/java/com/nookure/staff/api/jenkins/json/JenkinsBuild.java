package com.nookure.staff.api.jenkins.json;

import org.jetbrains.annotations.NotNull;

public record JenkinsBuild(
        @NotNull String _class, int number, @NotNull String url) {}
