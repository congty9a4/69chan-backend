package com.congty9a4.backend.mapper;

import com.congty9a4.backend.dto.resp.ConversationResponse;
import com.congty9a4.backend.dto.resp.MessageResponse;
import com.congty9a4.backend.entity.Conversation;
import com.congty9a4.backend.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ConversationMapper {
    ConversationResponse toResponse(Conversation conversation);
}
