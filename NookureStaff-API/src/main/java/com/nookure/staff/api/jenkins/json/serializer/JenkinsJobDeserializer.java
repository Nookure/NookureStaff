package com.nookure.staff.api.jenkins.json.serializer;

import static java.util.Objects.requireNonNull;

import com.google.gson.*;
import com.nookure.staff.api.jenkins.json.JenkinsBuild;
import com.nookure.staff.api.jenkins.json.JenkinsJob;
import java.lang.reflect.Type;

public class JenkinsJobDeserializer implements JsonDeserializer<JenkinsJob> {
    @Override
    public JenkinsJob deserialize(
            JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        return new JenkinsJob(
                requireNonNull(jsonObject.get("displayName").getAsString()),
                requireNonNull(jsonObject.get("fullDisplayName").getAsString()),
                requireNonNull(jsonObject.get("name").getAsString()),
                requireNonNull(jsonObject.get("url").getAsString()),
                jsonDeserializationContext.deserialize(jsonObject.get("builds"), JenkinsBuild[].class),
                jsonDeserializationContext.deserialize(jsonObject.get("lastCompletedBuild"), JenkinsBuild.class),
                jsonDeserializationContext.deserialize(jsonObject.get("lastStableBuild"), JenkinsBuild.class),
                jsonDeserializationContext.deserialize(jsonObject.get("lastSuccessfulBuild"), JenkinsBuild.class));
    }
}
