package com.congty9a4.backend.dto.resp;

import com.congty9a4.backend.entity.enums.MessageType;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class WsChatMessage {
    private String receiverId;
    private String content;
    private MessageType type;
}
