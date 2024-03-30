package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldNotFindUserById() {
        User user = new User(0L, "User", "user@ya.ru");
        Long id = userService.saveUser(user).getId();
        user.setId(id);
        assertEquals(user, userService.getUserById(id));
    }
}
