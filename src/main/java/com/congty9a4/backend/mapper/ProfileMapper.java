package com.congty9a4.backend.mapper;

import com.congty9a4.backend.dto.req.ProfileCreationRequest;
import com.congty9a4.backend.dto.req.ProfileUpdateRequest;
import com.congty9a4.backend.dto.resp.ProfileResponse;
import com.congty9a4.backend.entity.Profile;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @Builder(disableBuilder = true)
)
public interface ProfileMapper {

    @Mapping(source = "user.id", target = "infochan.userId")
    @Mapping(source = "user.username", target = "infochan.username")
    @Mapping(target = "infochan.profilePicture", ignore = true)
    @Mapping(source = "fullName", target = "infochan.fullName")
    ProfileResponse toProfileResponse(Profile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "birthday", expression = "java(request.getBirthday() != null ? java.time.LocalDate.parse(request.getBirthday()) : null)")
    Profile toProfile(ProfileCreationRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void update(@MappingTarget Profile profile, ProfileUpdateRequest request);
}

