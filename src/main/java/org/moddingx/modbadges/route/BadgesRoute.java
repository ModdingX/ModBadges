package org.moddingx.modbadges.route;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.function.Function;

public abstract class BadgesRoute<T> implements Route {

    protected final Service spark;
    private final String content;
    private final Function<T, String> resultFunc;

    protected BadgesRoute(Service spark, String content, Function<T, String> resultFunc) {
        this.spark = spark;
        this.content = content;
        this.resultFunc = resultFunc;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            String result = this.resultFunc.apply(this.apply(request, response));
            response.status(result == null ? 204 : 200);
            if (result != null) {
                response.header("Content-Type", this.content);
                response.header("Cache-Control", "no-cache");
            }

            return result;
        } catch (FileNotFoundException e) {
            throw this.spark.halt(404, "Not Found");
        }
    }

    protected abstract T apply(Request request, Response response) throws IOException;

    protected final String param(Request request, String key) {
        String value = request.params(":" + key);
        if (value == null) {
            throw this.spark.halt(500, "Missing key: " + key);
        }

        return value;
    }
}
