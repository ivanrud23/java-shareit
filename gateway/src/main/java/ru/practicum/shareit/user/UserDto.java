package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;
    @NotBlank(message = "email is mandatory")
    @Email
    private String email;
    @NotBlank(message = "login is mandatory")
    private String name;
}
