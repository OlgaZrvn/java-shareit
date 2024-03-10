package ru.practicum.shareit.user;

import java.util.List;

interface UserService {

    User saveUser(User user);
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}
