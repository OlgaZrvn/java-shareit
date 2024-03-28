package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testSaveOneUser() {
        User user = new User(0L, "user@ya.ru", "User1");
        when(userRepository.save(Mockito.any())).thenReturn(user);
        User savedUser = userService.saveUser(user);
        assertEquals(user, savedUser);
    }

    @Test
    void testGet2Users() {
        List<User> users = new ArrayList<>();
        User user1 = new User(0L, "user1@ya.ru", "User1");
        User user2 = new User(1L, "user2@ya.ru", "User2");
        users.add(user1);
        users.add(user2);
        when(userRepository.findAll()).thenReturn(users);
        List<User> savedUsers = userService.getAllUsers();
        assertEquals(2, savedUsers.size());
    }

    @Test
    void testGetUserById() {
        User user = new User(0L, "user@ya.ru", "User1");
        when(userRepository.save(Mockito.any())).thenReturn(user);
        Long id = userService.saveUser(user).getId();
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        User returnedUser = userService.getUserById(id);
        assertEquals(user, returnedUser);
    }

    @Test
    void testNotGetUserById() {
        assertThrows(NotFoundException.class, () -> userService.getUserById(0L));
    }

    @Test
    void testUpdateUser() {
        User user = new User(0L, "user@ya.ru", "User1");
        User updatedUser = new User("updatedUser@ya.ru", "UpdatedUser1");
        when(userRepository.save(Mockito.any())).thenReturn(updatedUser);
        Long id = userService.saveUser(user).getId();
        User returnedUser = userService.updateUser(id, updatedUser);
        assertEquals(updatedUser, returnedUser);
    }

    @Test
    void testDeleteUser() {
        User user = new User(0L, "user@ya.ru", "User1");
        when(userRepository.save(Mockito.any())).thenReturn(user);
        userService.saveUser(user);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        userService.deleteUser(0L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testNotDeleteUserWithNoUser() {
        assertThrows(NotFoundException.class, () -> userService.deleteUser(0L));
    }

}