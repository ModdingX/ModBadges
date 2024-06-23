package org.moddingx.modbadges.platform;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;

public class CurseApi extends PlatformApi {

    private final String token;

    public CurseApi(String token) {
        this.token = token;
    }

    @Override
    public String apiUrl() {
        return "https://api.curseforge.com/v1/";
    }

    @Override
    public int downloadCount(String projectId) throws IOException {
        try {
            HttpRequest httpRequest = this.httpRequest("mods/" + projectId);
            String body = this.client().send(httpRequest, info -> HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8)).body();
            if (body.isEmpty()) {
                return -1;
            }

            try {
                JsonObject json = GSON.fromJson(body, JsonObject.class);
                return json.getAsJsonObject("data").get("downloadCount").getAsInt();
            } catch (JsonParseException e) {
                throw new IOException("Failed to parse data from Curse API", e);
            }
        } catch (InterruptedException e) {
            throw new IOException("Interrupted", e);
        }
    }

    @Override
    public List<String> gameVersions(String projectId) throws IOException {
        try {
            HttpRequest httpRequest = this.httpRequest("mods/" + projectId);
            String body = this.client().send(httpRequest, info -> HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8)).body();

            if (body.isEmpty()) {
                return List.of();
            }

            try {
                JsonObject json = GSON.fromJson(body, JsonObject.class);
                Function<JsonElement, String> elementToString = jsonElement -> jsonElement.getAsJsonObject().get("gameVersion").getAsString();
                return this.stripVersionsArray(json.getAsJsonObject("data").get("latestFilesIndexes").getAsJsonArray(), elementToString);
            } catch (JsonParseException e) {
                throw new IOException("Failed to parse data from Curse API", e);
            }
        } catch (InterruptedException e) {
            throw new IOException("Interrupted", e);
        }
    }

    @Override
    protected HttpRequest httpRequest(String endpoint) {
        return HttpRequest.newBuilder().GET()
                .uri(URI.create(this.apiUrl() + endpoint))
                .header("Accept", "application/json")
                .header("User-Agent", "Java" + System.getProperty("java.version") + "/ModBadges")
                .header("x-api-key", this.token)
                .build();
    }
}
