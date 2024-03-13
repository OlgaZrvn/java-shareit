package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    @NotEmpty(message = "Email не может быть пустым")
    @Email(message = "Неверно указан Email")
    private String email;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
