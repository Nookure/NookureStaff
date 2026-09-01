package com.nookure.staff.api.jenkins.json.serializer;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import com.nookure.staff.api.jenkins.json.JenkinsArtifact;
import java.lang.reflect.Type;

public class JenkinsArtifactDeserializer implements JsonDeserializer<JenkinsArtifact> {
    @Override
    public JenkinsArtifact deserialize(
            JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        final String displayPath = jsonObject.get("displayPath").getAsString();
        final String fileName = jsonObject.get("fileName").getAsString();
        final String relativePath = jsonObject.get("relativePath").getAsString();

        Preconditions.checkNotNull(displayPath, "displayPath cannot be null");
        Preconditions.checkNotNull(fileName, "fileName cannot be null");
        Preconditions.checkNotNull(relativePath, "relativePath cannot be null");

        return new JenkinsArtifact(displayPath, fileName, relativePath);
    }
}
