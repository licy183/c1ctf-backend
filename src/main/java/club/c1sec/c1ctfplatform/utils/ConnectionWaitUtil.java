package club.c1sec.c1ctfplatform.utils;

import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.util.Properties;

public class ConnectionWaitUtil {
    public static void waitForDatabase() {
        YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
        yamlFactory.setResources(new ClassPathResource("application.yml"));
        Properties props = yamlFactory.getObject();
        String profile = props.getProperty("spring.profiles.active");

        yamlFactory.setDocumentMatchers(new YamlProcessor.DocumentMatcher() {
            @Override
            public YamlProcessor.MatchStatus matches(Properties properties) {
                String name = properties.getProperty("spring.profiles");
                if (profile.equals(name)) {
                    return YamlProcessor.MatchStatus.FOUND;
                } else {
                    return YamlProcessor.MatchStatus.NOT_FOUND;
                }
            }
        });

        props = yamlFactory.getObject();
        String mysqlDsn = props.getProperty("spring.datasource.url");
        String redisHost = props.getProperty("spring.redis.host");
        int redisPort = Integer.parseInt(props.getProperty("spring.redis.port"));
        ConnectionWaitUtil.waitFor(mysqlDsn);
        ConnectionWaitUtil.waitFor(redisHost, redisPort);
    }

    public static void waitFor(String host, int port) {
        ConnectionWaitUtil.waitFor("wait://" + host + ":" + port);
    }

    public static void waitFor(String dsn) {
        if (dsn.startsWith("jdbc:")) {
            dsn = dsn.substring(5);
        }
        try {
            URI uri = new URI(dsn);
            String host = uri.getHost();
            if (host == null) {
                System.out.println("Host is null, please verify your domain.");
            }
            int port = uri.getPort();
            boolean success = false;

            while (!success) {
                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(host, port), 1);
                    socket.close();
                    System.out.println("Sleep three sec to ensure mysql started.");
                    Thread.sleep(3000);
                } catch (IOException e) {
                    System.out.println("Connection Timeout in " + host + ":" + port);
                    Thread.sleep(1000);
                    continue;
                }
                success = true;
            }
        } catch (Exception ignored) {

        }
    }
}
