package com.congty9a4.backend.mapper;

import com.congty9a4.backend.dto.req.user.UserUpdationRequest;
import com.congty9a4.backend.dto.resp.MessageResponse;
import com.congty9a4.backend.dto.resp.UserResponse;
import com.congty9a4.backend.entity.Infochan;
import com.congty9a4.backend.entity.Message;
import com.congty9a4.backend.entity.Userchan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MessageMapper {
    MessageResponse toMessageResponse(Message message);
}
