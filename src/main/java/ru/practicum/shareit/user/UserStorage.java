package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserStorage {
    User createUser(User newUser);

    User getUser(Long id);

    Collection<User> getAllUsers();

    void deleteUser(Long id);
}
