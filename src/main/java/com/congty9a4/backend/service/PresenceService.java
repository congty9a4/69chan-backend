package com.congty9a4.backend.service;

import java.util.List;
import java.util.Map;

public interface PresenceService {
    void handleHeartbeat(String userId);

    Map<String, Boolean> getUsersPresence(List<String> userIds);

    void markAsOffline(String userId);
}
