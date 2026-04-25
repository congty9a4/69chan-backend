package com.congty9a4.backend.dto.resp;

import com.congty9a4.backend.entity.enums.MessageStatus;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class WsAckMessage {
    private String messageId;
    private String receiverId;
    private MessageStatus status;
}
