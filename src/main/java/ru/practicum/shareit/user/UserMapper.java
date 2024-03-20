package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    User toUser(UserDto userDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    UserDto toUserDto(User user);
}
