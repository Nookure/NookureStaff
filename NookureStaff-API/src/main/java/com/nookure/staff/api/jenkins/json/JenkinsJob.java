package com.nookure.staff.api.jenkins.json;

public record JenkinsJob(
        String displayName,
        String fullDisplayName,
        String name,
        String url,
        JenkinsBuild[] builds,
        JenkinsBuild lastCompletedBuild,
        JenkinsBuild lastStableBuild,
        JenkinsBuild lastSuccessfulBuild) {}
