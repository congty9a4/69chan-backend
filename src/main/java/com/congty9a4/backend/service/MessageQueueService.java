package com.congty9a4.backend.service;

import com.congty9a4.backend.dto.resp.WsChatMessage;

public interface MessageQueueService {
    void pushToQueue(String senderId, WsChatMessage payload);
}