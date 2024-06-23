package org.moddingx.modbadges;

import org.moddingx.modbadges.platform.CurseApi;
import org.moddingx.modbadges.platform.ModrinthApi;
import org.moddingx.modbadges.route.PlatformBadgeRoute;
import org.moddingx.modbadges.route.VersionRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;

import java.nio.file.Path;

public class BadgesServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BadgesServer.class);

    private final String version;
    private final Service spark;

    public BadgesServer(String version, int port, SslData ssl, int threads, String curseToken) {
        LOGGER.info("Starting Server on port {}", port);
        this.version = version;
        this.spark = Service.ignite();
        this.spark.port(port);
        LOGGER.info("Running on {} threads", threads);
        this.spark.threadPool(threads, threads, -1);
        if (ssl != null) {
            this.spark.secure(ssl.cert().toAbsolutePath().normalize().toString(), ssl.key(), null, null);
        } else {
            LOGGER.warn("Running without SSL");
        }

        ModrinthApi modrinthApi = new ModrinthApi();
        CurseApi curseApi = new CurseApi(curseToken);
        this.spark.get("/version", new VersionRoute(this.spark, this.version));
        this.spark.get("/modrinth/versions/:projectId", new PlatformBadgeRoute(this.spark, BadgeGenerator.Platform.MODRINTH, BadgeGenerator.BadgeType.GAME_VERSIONS, modrinthApi));
        this.spark.get("/modrinth/downloads/:projectId", new PlatformBadgeRoute(this.spark, BadgeGenerator.Platform.MODRINTH, BadgeGenerator.BadgeType.DOWNLOADS, modrinthApi));
        this.spark.get("/curseforge/versions/:projectId", new PlatformBadgeRoute(this.spark, BadgeGenerator.Platform.CURSEFORGE, BadgeGenerator.BadgeType.GAME_VERSIONS, curseApi));
        this.spark.get("/curseforge/downloads/:projectId", new PlatformBadgeRoute(this.spark, BadgeGenerator.Platform.CURSEFORGE, BadgeGenerator.BadgeType.DOWNLOADS, curseApi));
    }

    public String version() {
        return this.version;
    }

    public void shutdown() {
        this.spark.stop();
    }

    public record SslData(String key, Path cert) {}
}
