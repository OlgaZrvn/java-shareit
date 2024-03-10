package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private Long userId = 1L;
    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(Long id) {
        return users.get(id);
    }

    @Override
    public User save(User user) {
        user.setId(userId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(Long id, User user) {
        user.setId(id);
        if (user.getName() != null) {
            users.get(id).setName(user.getName());
        }
        if ((user.getEmail() != null) && (users.get(id).getEmail() != user.getEmail())) {
            users.get(id).setEmail(user.getEmail());
        }
        return users.get(id);
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId(), user);
    }
}