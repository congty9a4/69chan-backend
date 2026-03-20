package com.congty9a4.backend.mapper;

import com.congty9a4.backend.dto.req.user.UserUpdationRequest;
import com.congty9a4.backend.dto.resp.UserResponse;
import com.congty9a4.backend.entity.Infochan;
import com.congty9a4.backend.entity.Userchan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
    UserResponse toUserResponse(Userchan user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void update(@MappingTarget Userchan userchan, UserUpdationRequest updatedUserchan);

    @Mapping(source = "profile.keyName", target = "keyName")
    @Mapping(source = "profile.avatarUrl", target = "profilePicture")
    @Mapping(source = "id", target = "userId")
    Infochan toInfochan(Userchan userchan);

}
