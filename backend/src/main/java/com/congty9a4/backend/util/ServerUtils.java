package com.congty9a4.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerUtils {

    @Value("${server.address:localhost}")
    private String serverHost;

    @Value("${server.port:8080}")
    private String serverPort;

    public String getServerUrl() {
        return "http://" + serverHost + ":" + serverPort;
    }
}
