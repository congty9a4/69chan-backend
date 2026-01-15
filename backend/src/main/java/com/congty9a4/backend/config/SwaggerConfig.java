package com.congty9a4.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${app.swagger.server.url:}")
    private String customServerUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Info info = new Info()
                .title("69chan API")
                .version("1.0")
                .description("""
                        69chan is a social network app for neet and nerd
                        ```
                                                                ██████             \s
                                    ██████                    ████▒▒████           \s
                                  ████▒▒████████████████████████████▒▒██           \s
                                ██░░████▒▒██░░░░░░░░░░░░░░░░░░░░████▒▒████         \s
                              ██░░░░░░████▒▒██░░░░░░░░░░░░██░░░░░░░░████▒▒██       \s
                              ██░░░░░░██▒▒██░░░░░░░░░░░░░░░░██░░░░░░░░██▒▒██       \s
                            ██░░░░░░██▒▒██░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░████       \s
                            ██░░░░░░░░██░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░██       \s
                          ██░░░░░░████░░░░░░██░░░░░░░░░░░░░░██░░░░░░░░  ░░██       \s
                          ██░░░░░░██░░░░░░██░░░░░░░░░░░░░░░░██░░      ░░░░░░██     \s
                          ██░░░░░░██░░░░░░██░░      ░░    ██  ██░░░░░░░░░░░░██     \s
                        ██░░░░░░░░██░░░░░░██░░░░░░░░██░░░░██  ████░░░░██░░░░██     \s
                        ██░░░░░░░░██░░░░██░░░░░░░░██░░░░██      ██░░░░░░██░░██     \s
                        ██░░░░░░░░██░░░░██░░░░░░██░░░░░░██    ██████░░░░████       \s
                        ██░░░░░░░░██░░████░░░░██████████      ██  ██░░░░████       \s
                        ██░░░░░░░░██░░████████  ████  ██      ▒▒  ██░░░░██         \s
                        ██░░░░░░░░████████▒▒░░    ▒▒          ░░  ██░░████████  ██ \s
                        ██░░░░░░░░████░░██▒▒░░  ▒▒░░          ▒▒  ██░░████  ████  ██
                        ██░░░░░░░░██  ████▒▒░░  ▒▒▒▒              ████░░██  ██    ██
                        ██░░░░░░░░██    ██░░░░                  ████░░░░██      ██ \s
                        ██░░░░░░░░██      ████░░            ██████░░░░░░██    ██   \s
                        ██░░░░░░░░░░██        ██▓▓▓▓▓▓▓▓▓▓░░▓▓  ██░░░░██    ██     \s
                        ██░░░░░░░░░░██      ██    ▓▓▓▓▓▓▓▓░░▓▓██░░████    ██       \s
                        ██░░░░░░░░░░██    ██░░░░██▓▓▓▓▓▓▓▓░░▓▓██████    ████       \s
                        ██░░░░░░░░░░██  ████████░░░░▓▓▓▓░░██░░▓▓██    ██░░░░██     \s
                        ██░░░░░░░░░░░░██████████████░░░░██████░░██  ██░░░░░░██     \s
                          ██░░░░░░░░░░████  ████████████████████▓▓██░░░░░░░░██     \s
                          ██░░░░░░░░░░██  ████░░░░░░░░░░░░░░░░░░████░░░░░░░░██     \s
                            ██░░░░░░░░██      ██████████████████    ██░░░░██       \s
                              ██░░░░██      ████████      ██░░██      ████         \s
                                ████        ██░░██          ████                   \s
                                              ██                                   \s
                        ```
                        """);

        OpenAPI openAPI = new OpenAPI().info(info);

        var servers = new LinkedList<Server>();

        if (customServerUrl != null && !customServerUrl.isEmpty()) {
            servers.add(new Server()
                    .url(customServerUrl)
                    .description("Custom Server (ngrok/production)"));
        }
        servers.add(new Server()
                    .url("http://localhost:" + serverPort)
                    .description("HTTP Server"));
        openAPI.servers(servers.stream().toList());
        return openAPI;
    }
}

