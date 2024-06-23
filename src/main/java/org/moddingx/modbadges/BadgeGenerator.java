package org.moddingx.modbadges;

import java.util.Locale;
import java.util.function.Function;

public class BadgeGenerator {

    private static final String ERROR_BADGE = """
            <svg xmlns="http://www.w3.org/2000/svg" width="100" height="20">
              <rect width="100" height="20" fill="#FF0000" rx="3"/>
              <text x="50" y="14" fill="#FFFFFF" text-anchor="middle" font-family="DejaVu Sans,Verdana,Geneva,sans-serif" font-size="11">ERROR</text>
            </svg>
            """;

    public static String generateBadge(BadgeData data) {
        if (data.isError()) {
            return ERROR_BADGE;
        }

        return data.badgeStyle.getBadge().apply(data);
    }

    public record BadgeData(Platform platform, BadgeType badgeType, BadgeStyle badgeStyle, String message) {
        public static final BadgeData ERROR = new BadgeData(null, null, null, null);

        public boolean isError() {
            return this == ERROR || this.platform == null || this.badgeType == null || this.badgeStyle == null || this.message == null;
        }
    }

    public enum BadgeStyle {
        DEFAULT(BadgeTemplates.DEFAULT_BADGE),
        FLAT(BadgeTemplates.FLAT_BADGE);

        private final Function<BadgeData, String> badge;

        BadgeStyle(Function<BadgeData, String> badge) {
            this.badge = badge;
        }

        public Function<BadgeData, String> getBadge() {
            return this.badge;
        }

        public static BadgeStyle getBadgeStyle(String id) {
            for (BadgeStyle style : BadgeStyle.values()) {
                if (style.name().equals(id.toUpperCase(Locale.ROOT))) {
                    return style;
                }
            }

            return DEFAULT;
        }
    }

    public enum BadgeType {
        DOWNLOADS("Downloads", 8),
        GAME_VERSIONS("MC Versions", 6);

        private final String label;
        private final int widthMultiplier;

        BadgeType(String label, int widthMultiplier) {
            this.label = label;
            this.widthMultiplier = widthMultiplier;
        }

        public String getLabel() {
            return this.label;
        }

        public int getWidthMultiplier() {
            return this.widthMultiplier;
        }
    }

    public enum Platform {
        MODRINTH("00AF5C", 14, "PHN2ZyBmaWxsPSIjMDBBRjVDIiByb2xlPSJpbWciIHZpZXdCb3g9IjAgMCAyNCAyNCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48dGl0bGU+TW9kcmludGg8L3RpdGxlPjxwYXRoIGQ9Ik0xMi4yNTIuMDA0YTExLjc4IDExLjc2OCAwIDAgMC04LjkyIDMuNzMgMTEgMTAuOTk5IDAgMCAwLTIuMTcgMy4xMSAxMS4zNyAxMS4zNTkgMCAwIDAtMS4xNiA1LjE2OWMwIDEuNDIuMTcgMi41LjYgMy43Ny4yNC43NTkuNzcgMS44OTkgMS4xNyAyLjUyOWExMi4zIDEyLjI5OCAwIDAgMCA4Ljg1IDUuNjM5Yy40NC4wNSAyLjU0LjA3IDIuNzYuMDIuMi0uMDQuMjIuMS0uMjYtMS43bC0uMzYtMS4zNy0xLjAxLS4wNmE4LjUgOC40ODkgMCAwIDEtNS4xOC0xLjggNS4zNCA1LjM0IDAgMCAxLTEuMy0xLjI2YzAtLjA1LjM0LS4yOC43NC0uNWEzNy41NzIgMzcuNTQ1IDAgMCAxIDIuODgtMS42MjljLjAzIDAgLjUuNDUgMS4wNi45OGwxIC45NyAyLjA3LS40MyAyLjA2LS40MyAxLjQ3LTEuNDdjLjgtLjggMS40OC0xLjUgMS40OC0xLjUyIDAtLjA5LS40Mi0xLjYzLS40Ni0xLjctLjA0LS4wNi0uMi0uMDMtMS4wMi4xOC0uNTMuMTMtMS4yLjMtMS40NS40bC0uNDguMTUtLjUzLjUzLS41My41My0uOTMuMS0uOTMuMDctLjUyLS41YTIuNyAyLjcgMCAwIDEtLjk2LTEuN2wtLjEzLS42LjQzLS41N2MuNjgtLjkuNjgtLjkgMS40Ni0xLjEuNC0uMS42NS0uMi44My0uMzMuMTMtLjA5OS42NS0uNTc5IDEuMTQtMS4wNjlsLjktLjktLjctLjctLjctLjctMS45NS41NGMtMS4wNy4zLTEuOTYuNTMtMS45Ny41My0uMDMgMC0yLjIzIDIuNDgtMi42MyAyLjk3bC0uMjkuMzUuMjggMS4wM2MuMTYuNTYuMyAxLjE2LjMxIDEuMzRsLjAzLjMtLjM0LjIzYy0uMzcuMjMtMi4yMiAxLjMtMi44NCAxLjYzLS4zNi4yLS4zNy4yLS40NC4xLS4wOC0uMS0uMjMtLjYtLjMyLTEuMDMtLjE4LS44Ni0uMTctMi43NS4wMi0zLjczYTguODQgOC44MzkgMCAwIDEgNy45LTYuOTNjLjQzLS4wMy43Ny0uMDguNzgtLjEuMDYtLjE3LjUtMi45OTkuNDctMy4wMzktLjAxLS4wMi0uMS0uMDItLjItLjAzWm0zLjY4LjY3Yy0uMiAwLS4zLjEtLjM3LjM4LS4wNi4yMy0uNDYgMi40Mi0uNDYgMi41MiAwIC4wNC4xLjExLjIyLjE2YTguNTEgOC40OTkgMCAwIDEgMi45OSAyIDguMzggOC4zNzkgMCAwIDEgMi4xNiAzLjQ0OSA2LjkgNi45IDAgMCAxIC40IDIuOGMwIDEuMDcgMCAxLjI3LS4xIDEuNzNhOS4zNyA5LjM2OSAwIDAgMS0xLjc2IDMuNzY5Yy0uMzIuNC0uOTggMS4wNi0xLjM3IDEuMzgtLjM4LjMyLTEuNTQgMS4xLTEuNyAxLjE0LS4xLjAzLS4xLjA2LS4wNy4yNi4wMy4xOC42NCAyLjU2LjcgMi43OGwuMDYuMDZhMTIuMDcgMTIuMDU4IDAgMCAwIDcuMjctOS40Yy4xMy0uNzcuMTMtMi41OCAwLTMuNGExMS45NiAxMS45NDggMCAwIDAtNS43My04LjU3OGMtLjctLjQyLTIuMDUtMS4wNi0yLjI1LTEuMDZaIi8+PC9zdmc+"),
        CURSEFORGE("F16436", 16, "DQo8c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmlld0JveD0iMCAwIDEwMCA1My41Ij48cGF0aCBmaWxsPSIjRjE2NDM2IiBmaWxsLXJ1bGU9ImV2ZW5vZGQiIGQ9Ik0wIDYuOTA4MmwyNy45NTMyLjA3NTZMMjYuMjY5IDBoNzMuNzQ0OWwtLjAxNjQgOC4xNDMzQzg3LjUgMTAuNjM0OCA3OC42NzI2IDE2Ljc3NSA3NC44MjUgMzYuMzRINzIuMDQ2bC0xLjU4MTUgNC4zNTloMi4zMDc5bDMuOTkwNiAxMi44MDg3SDM3Ljk2MWwzLjkxODQtMTIuODA4NmgyLjMyNWwtMS42NDY3LTQuMzU5aC0yLjc2NDJsLTUuMTUxNi0xMy43NTM1QzEzLjUyMDMgMjEuMzQzNyAyLjY3NDggMTYuOTA0MiAwIDYuOTA4MnpNNTguNjc3MiAzOS4wNjJjLTMuMDgyLTUuNjIxNi01LjEwNTgtMS4xOTctMTEuNDQ1NC02Ljk0OTYtMi40NzI3LTIuMjQzNi0zLjQ1ODgtOC44MTk5IDIuMDk3LTEwLjgzMDEtMi41MjU1IDUuNTc0OSA0LjAyOTcgNy44NTU4IDUuNTg5OCA0LjY2ODUuNzA5LTEuNDQ4NiAxLjYwMzktNC4xMjA0LS45Nzk4LTUuOTUxOC0xLjM5Mi0uOTg2Ny0xLjkyODEtMy45MTI1LS43MzY0LTUuNTM4OC4zODM3IDEuMjA5MyAxLjU5MzggMi41MDEzIDMuOTE2IDEuNTQxMS00LjkxOTItOC4xNDA0IDIuOTM0LTEwLjk0NTcgNi43MTA0LTEwLjU4MTctMy45Njg2LjQ0MS02LjAyNSAzLjk3Ny00LjIxOTYgNy44MjY5IDEuMTUgMi40NTIgMi45MDA2IDMuNjU4OSAzLjQwOTcgNS42NjU3LTEuNjk1Mi4xMjYzLTIuMTg1Mi45MDY1LTIuMTQ5NSAyLjM2Mi4wMjY4IDEuMDg5NSAyLjU0OTYgMi42NjQ2IDIuOTQ5NC0uNDQ1NiAxLjE3NSAyLjQyNDUtLjI0MDcgMy44NDk2LTEuMDEyOSA0Ljk5ODQtMS43ODI0IDIuNjUxNS0uMTAzNiA1LjQxMDcgMi4xMDk3IDMuMzA2Ny45NTQ3LS45MDc0IDEuNjE1NS0yLjU4MTIuNDA0OC01LjMxOCAyLjIyOSAxLjc3NiA0LjUwNTcgNi41NDQ3LjkwNzkgMTAuNjc3LTEuNzU2IDIuMDE3LTYuOTUxNCAxLjM5MS03LjU1MSA0LjU2OTN6Ii8+PC9zdmc+");

        private final String color;
        private final int iconSize;
        private final String icon;

        Platform(String color, int iconSize, String icon) {
            this.color = color;
            this.iconSize = iconSize;
            this.icon = icon;
        }

        public String getColor() {
            return this.color;
        }

        public int iconSize() {
            return this.iconSize;
        }

        public String getIcon() {
            return "data:image/svg+xml;base64," + this.icon;
        }
    }
}
