package com.congty9a4.backend.dto.resp;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class WsTypingIndicator {
    private String receiverId;
    private boolean isTyping;
}
