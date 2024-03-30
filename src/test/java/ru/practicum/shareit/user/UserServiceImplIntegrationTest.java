package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void testNotFindUserById() {
       /* User user = new User("User", "user@ya.ru");
        Long id = userService.saveUser(user).getId();
        assertEquals(user, userService.getUserById(id));

        */
    }
}
