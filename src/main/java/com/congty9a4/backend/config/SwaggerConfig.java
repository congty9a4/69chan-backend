package com.congty9a4.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.LinkedList;


@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${app.swagger.server.url:}")
    private String customServerUrl;

    private static final String[] WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/auth/**",
            "/api/users/create",
            "/api/sample/**",
            "/api/files/**",
            "/",
            "/api/v1/**",
            "/actuator/**"
    };

    @Bean
    public OpenAPI myOpenAPI() {
        final String securitySchemeName = "bearerAuth";
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
                          ██░░░░░░░░░░████  ██████████████████████▓▓██░░░░░░░░██     \s
                          ██░░░░░░░░░░██  ████░░░░░░░░░░░░░░░░░░████░░░░░░░░██     \s
                            ██░░░░░░░░██      ██████████████████    ██░░░░██       \s
                              ██░░░░██      ████████      ██░░██      ████         \s
                                ████        ██░░██          ████                   \s
                                              ██                                   \s
                        ```
                        """);

        OpenAPI openAPI = new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(info);

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

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            String endpoint = getEndpoint(handlerMethod);
            boolean isWhitelisted = Arrays.stream(WHITELIST)
                    .anyMatch(pattern -> endpoint.matches(pattern.replace("/**", ".*")));

            if (isWhitelisted) {
                operation.setSecurity(null);
            }

            return operation;
        };
    }

    private String getEndpoint(HandlerMethod handlerMethod) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), RequestMapping.class);
        String classPath = (requestMapping != null && requestMapping.value().length > 0) ? requestMapping.value()[0] : "";

        RequestMapping methodMapping = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getMethod(), RequestMapping.class);
        String methodPath = (methodMapping != null && methodMapping.value().length > 0) ? methodMapping.value()[0] : "";

        return (classPath + methodPath).replaceAll("/+", "/");
    }
}
