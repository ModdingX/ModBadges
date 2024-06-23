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

public class ModrinthApi extends PlatformApi {

    @Override
    public String apiUrl() {
        return "https://api.modrinth.com/v3/";
    }

    @Override
    public int downloadCount(String projectId) throws IOException {
        try {
            HttpRequest httpRequest = this.httpRequest("project/" + projectId);
            String body = this.client().send(httpRequest, response -> HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8)).body();
            if (body.isEmpty()) {
                return -1;
            }

            try {
                JsonObject json = GSON.fromJson(body, JsonObject.class);
                JsonElement downloads = json.get("downloads");
                return downloads != null ? downloads.getAsInt() : -1;
            } catch (JsonParseException e) {
                throw new IOException("Failed to parse data from Modrinth API", e);
            }
        } catch (InterruptedException e) {
            throw new IOException("Interrupted", e);
        }
    }

    @Override
    public List<String> gameVersions(String projectId) throws IOException {
        try {
            HttpRequest httpRequest = this.httpRequest("project/" + projectId);
            String body = this.client().send(httpRequest, response -> HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8)).body();
            if (body.isEmpty()) {
                return List.of();
            }

            try {
                JsonObject json = GSON.fromJson(body, JsonObject.class);
                JsonElement gameVersions = json.get("game_versions");
                return gameVersions != null
                        ? this.stripVersionsArray(gameVersions.getAsJsonArray(), JsonElement::getAsString)
                        : List.of();
            } catch (JsonParseException e) {
                throw new IOException("Failed to parse data from Modrinth API", e);
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
                .build();
    }
}
