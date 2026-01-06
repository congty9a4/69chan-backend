package com.congty9a4.backend.mapper;

import com.congty9a4.backend.dto.req.user.UserUpdationRequest;
import com.congty9a4.backend.dto.resp.user.UserResponse;
import com.congty9a4.backend.entity.post.Infochan;
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

    void update(@MappingTarget Userchan userchan, UserUpdationRequest updatedUserchan);

    @Mapping(source = "profile.fullName", target = "fullname")
    @Mapping(source = "profile.avatarUrl", target = "profilePicture")
    Infochan toInfochan(Userchan userchan);

}
