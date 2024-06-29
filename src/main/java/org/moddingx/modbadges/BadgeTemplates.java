package org.moddingx.modbadges;

import java.util.function.Function;

public class BadgeTemplates {

    public static final Function<BadgeGenerator.BadgeData, String> DEFAULT_BADGE = data -> {
        int labelLength = data.badgeType().getLabel().length();
        int messageLength = data.message().length();

        int labelWidth = Math.max(40, labelLength * 8) + 16;
        int messageWidth = Math.max(50, messageLength * data.badgeType().getWidthMultiplier());
        int totalWidth = labelWidth + messageWidth;
        return "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" width=\"" + totalWidth + "\" height=\"20\">\n" +
                "  <mask id=\"a\">\n" +
                "    <rect width=\"" + totalWidth + "\" height=\"20\" rx=\"3\" fill=\"#fff\"/>\n" +
                "  </mask>\n" +
                "  <g mask=\"url(#a)\">\n" +
                "    <rect width=\"" + labelWidth + "\" height=\"20\" fill=\"#555\"/>\n" +
                "    <rect x=\"" + labelWidth + "\" width=\"" + messageWidth + "\" height=\"20\" fill=\"#" + data.platform().getColor() + "\"/>\n" +
                "    <rect width=\"" + totalWidth + "\" height=\"20\" fill=\"url(#b)\"/>\n" +
                "  </g>\n" +
                "  <g fill=\"#fff\" text-anchor=\"middle\" font-family=\"DejaVu Sans,Verdana,Geneva,sans-serif\" font-size=\"11\">\n" +
                "    <image x=\"4\" y=\"" + ((20 - data.platform().iconSize()) / 2) + "\" width=\"" + data.platform().iconSize() + "\" height=\"" + data.platform().iconSize() + "\" xlink:href=\"" + data.platform().getIcon() + "\"/>\n" +
                "    <text x=\"" + (labelWidth / 2 + 10) + "\" y=\"15\" fill=\"#010101\" fill-opacity=\".3\">" + data.badgeType().getLabel() + "</text>\n" +
                "    <text x=\"" + (labelWidth / 2 + 10) + "\" y=\"14\">" + data.badgeType().getLabel() + "</text>\n" +
                "    <text x=\"" + (labelWidth + messageWidth / 2) + "\" y=\"15\" fill=\"#010101\" fill-opacity=\".3\">" + data.message() + "</text>\n" +
                "    <text x=\"" + (labelWidth + messageWidth / 2) + "\" y=\"14\">" + data.message() + "</text>\n" +
                "  </g>\n" +
                "</svg>";
    };

    public static final Function<BadgeGenerator.BadgeData, String> FLAT_BADGE = data -> {
        int labelLength = data.badgeType().getLabel().length();
        int messageLength = data.message().length();

        int labelWidth = Math.max(40, labelLength * 8) + 16;
        int messageWidth = Math.max(50, messageLength * data.badgeType().getWidthMultiplier());
        int totalWidth = labelWidth + messageWidth;
        return "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" width=\"" + totalWidth + "\" height=\"20\">\n" +
                "  <linearGradient id=\"b\" x2=\"0\" y2=\"100%\">\n" +
                "    <stop offset=\"0\" stop-color=\"#bbb\" stop-opacity=\".1\"/>\n" +
                "    <stop offset=\"1\" stop-opacity=\".1\"/>\n" +
                "  </linearGradient>\n" +
                "  <g mask=\"url(#a)\">\n" +
                "    <rect width=\"" + labelWidth + "\" height=\"20\" fill=\"#555\"/>\n" +
                "    <rect x=\"" + labelWidth + "\" width=\"" + messageWidth + "\" height=\"20\" fill=\"#" + data.platform().getColor() + "\"/>\n" +
                "    <rect width=\"" + totalWidth + "\" height=\"20\" fill=\"url(#b)\"/>\n" +
                "  </g>\n" +
                "  <g fill=\"#fff\" text-anchor=\"middle\" font-family=\"DejaVu Sans,Verdana,Geneva,sans-serif\" font-size=\"11\">\n" +
                "    <image x=\"4\" y=\"" + ((20 - data.platform().iconSize()) / 2) + "\" width=\"" + data.platform().iconSize() + "\" height=\"" + data.platform().iconSize() + "\" xlink:href=\"" + data.platform().getIcon() + "\"/>\n" +
                "    <text x=\"" + (labelWidth / 2 + 10) + "\" y=\"15\" fill=\"#010101\" fill-opacity=\".3\">" + data.badgeType().getLabel() + "</text>\n" +
                "    <text x=\"" + (labelWidth / 2 + 10) + "\" y=\"14\">" + data.badgeType().getLabel() + "</text>\n" +
                "    <text x=\"" + (labelWidth + messageWidth / 2) + "\" y=\"15\" fill=\"#010101\" fill-opacity=\".3\">" + data.message() + "</text>\n" +
                "    <text x=\"" + (labelWidth + messageWidth / 2) + "\" y=\"14\">" + data.message() + "</text>\n" +
                "  </g>\n" +
                "</svg>";
    };
}
