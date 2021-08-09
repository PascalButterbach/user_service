package de.chronies.user.service.mapper;

import de.chronies.user.service.dto.UserDto;
import de.chronies.user.service.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "user.user_id", target = "id")
    @Mapping(source = "user.user_name", target = "login")
    @Mapping(source = "token", target = "token")
    UserDto toUserDto(User user, String token);
}