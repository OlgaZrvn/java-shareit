package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;

    private final UserMapper userMapper = new UserMapperImpl();

    @Test
    void shouldUpdateUser() {
        User user = new User(0L, "User", "user@ya.ru");
        User user2 = new User(0L, "UpdatedUser", "user@ya.ru");
        UserDto userDto = userMapper.toUserDto(user2);
        User updatedUser = userService.updateUser(user.getId(), userMapper.toUser(userDto));
        user2.setId(updatedUser.getId());
        assertEquals(user2, updatedUser);
    }
}
