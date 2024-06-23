package org.moddingx.modbadges.platform;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public abstract class PlatformApi {

    protected static final Gson GSON;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.disableHtmlEscaping();
        GSON = builder.create();
    }

    protected HttpClient client() {
        return HttpClient.newHttpClient();
    }

    public abstract String apiUrl();

    public final String formattedDownloadCount(String projectId) throws IOException {
        int downloadCount = this.downloadCount(projectId);
        return downloadCount == -1 ? null : NumberFormat.getNumberInstance(Locale.US).format(downloadCount);
    }

    public abstract int downloadCount(String projectId) throws IOException;

    public final String formattedGameVersions(String projectId) throws IOException {
        List<String> gameVersions = this.gameVersions(projectId);
        return String.join(" | ", gameVersions);
    }

    public abstract List<String> gameVersions(String projectId) throws IOException;

    protected abstract HttpRequest httpRequest(String endpoint);

    protected final List<String> stripVersionsArray(JsonArray array, Function<JsonElement, String> elementToString) {
        return array.asList().stream()
                .map(elementToString)
                .map(s -> {
                    String[] parts = s.split("\\.");
                    return parts[0] + "." + parts[1];
                })
                .distinct()
                .sorted()
                .toList();
    }
}
