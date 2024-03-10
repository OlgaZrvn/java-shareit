package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.Throwable;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User saveUser(User user) {
        validateEmail(user);
        log.info("Создан новый пользователь {}", user.getName());
        return repository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return repository.findById(id);
    }

    @Override
    public User updateUser(Long id, User user) {
        validateUpdateEmail(user);
        log.info("Обновление пользователя с id {}", id);
        return repository.update(id, user);
    }

    @Override
    public void deleteUser(Long id) {
        userExists(id);
        log.info("Пользователь {} удален", getUserById(id).getName());
        repository.delete(getUserById(id));
    }

    private void validateEmail(User user) {
        List<User> users = new ArrayList(repository.findAll());
        if (users.stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new Throwable("Пользователь с таким Email уже существует");
        }
    }

    private void validateUpdateEmail(User user) {
        List<User> users = new ArrayList(repository.findAll());
        users.remove(user);
        if (users.stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new Throwable("Пользователь с таким Email уже существует");
        }
    }

    private void userExists(Long id) {
        if (null == getUserById(id)) {
            log.error("Пользователь не найден");
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
