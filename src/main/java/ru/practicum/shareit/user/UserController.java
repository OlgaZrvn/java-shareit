package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserDto> createNewUser(@Valid @RequestBody UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return ResponseEntity.ok().body(userMapper.toUserDto(userService.saveUser(user)));
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        User user;
        try {
            user = userService.getUserById(userId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        return ResponseEntity.ok().body(userMapper.toUserDto(user));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return ResponseEntity.ok().body(userMapper.toUserDto(userService.updateUser(userId, user)));
        }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
