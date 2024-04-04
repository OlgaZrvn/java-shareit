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

    private final User user = new User("User", "user@ya.ru");

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void shouldSaveNewUser() {
        when(userRepository.save(Mockito.any())).thenReturn(user);
        User savedUser = userService.saveUser(user);
        assertEquals(user, savedUser);
    }

    @Test
    void shouldGet2Users() {
        List<User> users = new ArrayList<>();
        User user1 = new User("User1", "user1@ya.ru");
        User user2 = new User("User2", "user2@ya.ru");
        users.add(user1);
        users.add(user2);
        when(userRepository.findAll()).thenReturn(users);
        List<User> savedUsers = userService.getAllUsers();
        assertEquals(2, savedUsers.size());
    }

    @Test
    void shouldGetUserById() {
        when(userRepository.save(Mockito.any())).thenReturn(user);
        userService.saveUser(user);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        User returnedUser = userService.getUserById(0L);
        assertEquals(user, returnedUser);
    }

    @Test
    void shouldNotGetUserById() {
        assertThrows(NotFoundException.class, () -> userService.getUserById(0L));
    }

    @Test
    void shouldUpdateUser() {
        User updatedUser = new User("updatedUser@ya.ru", "UpdatedUser1");
        when(userRepository.save(Mockito.any())).thenReturn(updatedUser);
        Long id = userService.saveUser(user).getId();
        User returnedUser = userService.updateUser(id, updatedUser);
        assertEquals(updatedUser, returnedUser);
    }

    @Test
    void shouldUpdateUserWithoutName() {
        User updatedUser = new User(0L, null, "updatedUser@ya.ru");
        when(userRepository.save(Mockito.any())).thenReturn(updatedUser);
        when(userRepository.getReferenceById(Mockito.anyLong())).thenReturn(user);
        User returnedUser = userService.updateUser(0L, updatedUser);
        updatedUser.setName(user.getName());
        assertEquals(updatedUser, returnedUser);
    }

    @Test
    void shouldUpdateUserWithoutEmail() {
        User updatedUser = new User(0L, "UpdatedUser", null);
        when(userRepository.save(Mockito.any())).thenReturn(updatedUser);
        when(userRepository.getReferenceById(Mockito.anyLong())).thenReturn(user);
        User returnedUser = userService.updateUser(0L, updatedUser);
        updatedUser.setEmail(user.getEmail());
        assertEquals(updatedUser, returnedUser);
    }

    @Test
    void shouldDeleteUser() {
        when(userRepository.save(Mockito.any())).thenReturn(user);
        userService.saveUser(user);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        userService.deleteUser(0L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void shouldNotDeleteUserWithoutUser() {
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.deleteUser(0L));
        assertEquals("Пользователь с id " + 0L + " не найден", exception.getMessage());
    }

}