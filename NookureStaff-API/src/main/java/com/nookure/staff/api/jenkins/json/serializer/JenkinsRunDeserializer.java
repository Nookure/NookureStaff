package com.nookure.staff.api.jenkins.json.serializer;

import static java.util.Objects.requireNonNull;

import com.google.gson.*;
import com.nookure.staff.api.jenkins.json.JenkinsArtifact;
import com.nookure.staff.api.jenkins.json.JenkinsRun;
import java.lang.reflect.Type;

public class JenkinsRunDeserializer implements JsonDeserializer<JenkinsRun> {
    @Override
    public JenkinsRun deserialize(
            JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String description =
                jsonObject.has("description") && !jsonObject.get("description").isJsonNull()
                        ? jsonObject.get("description").getAsString()
                        : null;

        return new JenkinsRun(
                requireNonNull(jsonObject.get("_class").getAsString()),
                jsonObject.get("building").getAsBoolean(),
                description,
                jsonObject.get("duration").getAsLong(),
                requireNonNull(jsonObject.get("fullDisplayName").getAsString()),
                requireNonNull(jsonObject.get("url").getAsString()),
                jsonDeserializationContext.deserialize(jsonObject.get("artifacts"), JenkinsArtifact[].class));
    }
}
