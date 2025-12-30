package com.congty9a4.backend.mapper;

import com.congty9a4.backend.dto.resp.user.UserResponse;
import com.congty9a4.backend.entity.Userchan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
    UserResponse toUserResponse(Userchan user);
}
