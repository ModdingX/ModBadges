package org.moddingx.modbadges.route;

import spark.Request;
import spark.Response;
import spark.Service;

import java.util.function.Function;

public class VersionRoute extends BadgesRoute<String> {

    private final String version;

    public VersionRoute(Service spark, String version) {
        super(spark, "text/plain", Function.identity());
        this.version = version;
    }

    @Override
    protected String apply(Request request, Response response) {
        return this.version;
    }
}
