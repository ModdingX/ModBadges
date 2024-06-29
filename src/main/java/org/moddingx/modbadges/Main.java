package org.moddingx.modbadges;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.util.PathConverter;
import joptsimple.util.PathProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.Manifest;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        String version;
        try (InputStream in = Main.class.getResourceAsStream("/META-INF/MANIFEST.MF")) {
            Manifest manifest = new Manifest(in);
            version = manifest.getMainAttributes().getValue("Implementation-Version");
        }

        OptionParser options = new OptionParser(false);
        OptionSpec<Void> specDocker = options.accepts("docker", "Run in Docker mode. This will load secrets as docker secrets.");

        OptionSpec<String> specToken = options.accepts("curse-token", "The CurseForge API token to use")
                .availableUnless(specDocker).requiredUnless(specDocker).withRequiredArg();

        OptionSpec<Void> specNoSsl = options.accepts("no-ssl", "Disable SSL. For testing only.");

        OptionSpec<Path> specSsl = options.accepts("ssl", "The SSL certificate fle to use.")
                .availableUnless(specDocker, specNoSsl).withRequiredArg()
                .withValuesConvertedBy(new PathConverter(PathProperties.FILE_EXISTING, PathProperties.READABLE));

        OptionSpec<String> specSslKey = options.accepts("ssl-key", "The password of the SSL certificate.")
                .availableUnless(specDocker, specNoSsl).withRequiredArg().defaultsTo("");

        OptionSpec<Integer> specPort = options.accepts("port", "The port to run on.").withRequiredArg().ofType(Integer.class);

        OptionSet set = options.parse(args);

        if (version == null) {
            version = "UNKNOWN";
            LOGGER.warn("Failed to detect version, falling back to {}", version);
        } else {
            LOGGER.info("Running ModBadges v{}", version);
        }

        boolean docker = set.has(specDocker);
        boolean useSsl = !set.has(specNoSsl);

        BadgesServer.SslData ssl = null;
        int port = set.has(specPort) ? set.valueOf(specPort) : (useSsl ? 443 : 80);

        if (useSsl) {
            ssl = new BadgesServer.SslData(
                    docker ? Main.dockerSecret("ssl_keystore_password") : set.valueOf(specSslKey),
                    docker ? Main.dockerSecretPath("ssl_keystore") : set.valueOf(specSsl).toAbsolutePath().normalize()
            );
        }
        String curseToken = docker ? Main.dockerSecret("curse_token") : set.valueOf(specToken);

        BadgesServer server = new BadgesServer(version, port, ssl, curseToken);
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        LOGGER.info("Initialisation complete.");
    }

    private static String dockerSecret(String id) throws IOException {
        return Files.readString(Main.dockerSecretPath(id));
    }

    private static Path dockerSecretPath(String id) {
        return Paths.get("/", "run", "secrets", id);
    }
}
