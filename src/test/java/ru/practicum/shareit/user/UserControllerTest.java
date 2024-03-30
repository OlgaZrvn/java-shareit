package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldSaveNewUser() throws Exception {
        User user = new User(0L,"User1", "user@ya.ru");
        UserDto userDto = new UserDto(0L, "User1", "user@ya.ru");
        when(userService.saveUser(Mockito.any(User.class))).thenReturn(user);
        when(userMapper.toUser(Mockito.any(UserDto.class))).thenReturn(user);
        when(userMapper.toUserDto(Mockito.any(User.class))).thenReturn(userDto);
        mvc.perform(post("/users")
                    .content(mapper.writeValueAsString(user))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void shouldGetTwoUsers() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User(0L,"User1", "user1@ya.ru"));
        users.add(new User(1L,"User2", "user2@ya.ru"));
        when(userService.getAllUsers()).thenReturn(users);

        mvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetUserById() throws Exception {
        User user = new User(0L,"User1", "user@ya.ru");
        UserDto userDto = new UserDto(0L, "User1", "user@ya.ru");
        when(userService.getUserById(Mockito.anyLong())).thenReturn(user);
        when(userMapper.toUserDto(Mockito.any(User.class))).thenReturn(userDto);
        mvc.perform(get("/users/0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void shouldDeleteUserById() throws Exception {
        mvc.perform(delete("/users/0"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        User user = new User(0L,"User1", "user@ya.ru");
        UserDto userDto = new UserDto(0L, "User1", "user@ya.ru");
        when(userService.updateUser(Mockito.anyLong(), Mockito.any(User.class))).thenReturn(user);
        when(userMapper.toUser(Mockito.any(UserDto.class))).thenReturn(user);
        when(userMapper.toUserDto(Mockito.any(User.class))).thenReturn(userDto);
        mvc.perform(patch("/users/0")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }
}