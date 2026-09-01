package com.nookure.staff.api.jenkins;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;
import com.nookure.staff.api.jenkins.json.JenkinsArtifact;
import com.nookure.staff.api.jenkins.json.JenkinsBuild;
import com.nookure.staff.api.jenkins.json.JenkinsJob;
import com.nookure.staff.api.jenkins.json.JenkinsRun;
import com.nookure.staff.api.jenkins.json.serializer.JenkinsArtifactDeserializer;
import com.nookure.staff.api.jenkins.json.serializer.JenkinsBuildDeserializer;
import com.nookure.staff.api.jenkins.json.serializer.JenkinsJobDeserializer;
import com.nookure.staff.api.jenkins.json.serializer.JenkinsRunDeserializer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

@Singleton
@AutoService(JenkinsBaseClient.class)
public class JenkinsBaseClient {
    private static final String agentName = "NookureStaff-API";

    private final String jenkinsUrl;
    private final URI jenkinsUri;
    private final HttpClient httpClient;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(JenkinsBuild.class, new JenkinsBuildDeserializer())
            .registerTypeAdapter(JenkinsArtifact.class, new JenkinsArtifactDeserializer())
            .registerTypeAdapter(JenkinsRun.class, new JenkinsRunDeserializer())
            .registerTypeAdapter(JenkinsJob.class, new JenkinsJobDeserializer())
            .create();

    public JenkinsBaseClient(@NotNull final String jenkinsUrl) {
        this.jenkinsUrl = jenkinsUrl;
        this.jenkinsUri = URI.create(jenkinsUrl);
        this.httpClient =
                HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    }

    public @NotNull CompletableFuture<JenkinsJob> getJob(@NotNull final String jobName) {
        return httpClient
                .sendAsync(
                        HttpRequest.newBuilder()
                                .uri(URI.create(jenkinsUrl + "/job/" + jobName + "/api/json"))
                                .header("User-Agent", agentName)
                                .build(),
                        HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> gson.fromJson(response.body(), JenkinsJob.class));
    }

    public @NotNull CompletableFuture<JenkinsRun> getRun(@NotNull final String jobName, final int runNumber) {
        return httpClient
                .sendAsync(
                        HttpRequest.newBuilder()
                                .uri(URI.create(jenkinsUrl + "/job/" + jobName + "/" + runNumber + "/api/json"))
                                .header("User-Agent", agentName)
                                .build(),
                        HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> gson.fromJson(response.body(), JenkinsRun.class));
    }

    public @NotNull CompletableFuture<JenkinsRun> getRun(@NotNull JenkinsJob job, final int runNumber) {
        return getRun(job.name(), runNumber);
    }

    public @NotNull HttpClient getHttpClient() {
        return httpClient;
    }

    public @NotNull String getJenkinsUrl() {
        return jenkinsUrl;
    }

    public @NotNull URI getJenkinsUri() {
        return jenkinsUri;
    }
}
