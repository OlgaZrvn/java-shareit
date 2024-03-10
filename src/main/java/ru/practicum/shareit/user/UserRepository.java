package ru.practicum.shareit.user;

import java.util.List;
import java.util.Map;

interface UserRepository {
    List<User> findAll();
    User findById(Long id);
    User save(User user);
    User update(Long id, User user);
    void delete(User user);
}