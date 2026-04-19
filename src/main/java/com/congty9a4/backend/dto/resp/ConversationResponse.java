package com.congty9a4.backend.dto.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ConversationResponse(
        String id,

        @JsonProperty("participant_ids")
        List<String> participantIds,

        @JsonProperty("last_message")
        String lastMessage,

        @JsonProperty("last_sender_id")
        String lastSenderId,

        @JsonProperty("last_message_time")
        String lastMessageTime,

        @JsonProperty("unread_count")
        int unreadCount

) {
}
