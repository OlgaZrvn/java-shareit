package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    @Transactional
    public User saveUser(User user) {
        user.setId(user.getId());
        log.info("Создан новый пользователь {} c id {}", user.getName(), user.getId());
        return repository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + id + " не найден"));
        log.info("Получен пользователь {} c id {}", user.getName(), user.getId());
        return user;
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        User updatedUser = new User();
        updatedUser.setId(id);

        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        } else {
            updatedUser.setName(repository.getReferenceById(id).getName());
        }

        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        } else {
            updatedUser.setEmail(repository.getReferenceById(id).getEmail());
        }

        log.info("Обновление пользователя с id {}", id);
        return repository.save(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        checkUser(id);
        log.info("Пользователь {} удален", getUserById(id).getName());
        repository.delete(getUserById(id));
    }

    private void checkUser(Long id) {
        if (null == getUserById(id)) {
            log.error("Пользователь не найден");
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
