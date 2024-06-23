package org.moddingx.modbadges.route;

import org.moddingx.modbadges.BadgeGenerator;
import org.moddingx.modbadges.platform.PlatformApi;
import spark.Request;
import spark.Response;
import spark.Service;

import java.io.IOException;

public class PlatformBadgeRoute extends BadgesRoute<BadgeGenerator.BadgeData> {

    private final BadgeGenerator.Platform platform;
    private final BadgeGenerator.BadgeType badgeType;
    private final PlatformApi platformApi;

    public PlatformBadgeRoute(Service spark, BadgeGenerator.Platform platform, BadgeGenerator.BadgeType badgeType, PlatformApi platformApi) {
        super(spark, "image/svg+xml", PlatformBadgeRoute::generateBadge);
        this.platform = platform;
        this.badgeType = badgeType;
        this.platformApi = platformApi;
    }

    @Override
    protected BadgeGenerator.BadgeData apply(Request request, Response response) throws IOException {
        String projectId = this.param(request, "projectId");
        BadgeGenerator.BadgeStyle badgeStyle = this.getBadgeStyle(request);

        String message = switch (this.badgeType) {
            case DOWNLOADS -> this.platformApi.formattedDownloadCount(projectId);
            case GAME_VERSIONS -> this.platformApi.formattedGameVersions(projectId);
        };

        return new BadgeGenerator.BadgeData(this.platform, this.badgeType, badgeStyle, message);
    }

    private static String generateBadge(BadgeGenerator.BadgeData data) {
        return BadgeGenerator.generateBadge(data);
    }
}
