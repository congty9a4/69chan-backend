package com.congty9a4.backend.mapper;

import com.congty9a4.backend.dto.resp.user.UserResponse;
import com.congty9a4.backend.entity.relational.Userchan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "username", target = "username")
    UserResponse toUserResponse(Userchan user);
}
