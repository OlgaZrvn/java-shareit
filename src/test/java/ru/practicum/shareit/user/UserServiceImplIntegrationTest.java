package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void testNotFindUserById() {
        User user = new User("user@ya.ru", "User1");
        Long id = userService.saveUser(user).getId();
        assertEquals(user, userService.getUserById(id));
    }

}