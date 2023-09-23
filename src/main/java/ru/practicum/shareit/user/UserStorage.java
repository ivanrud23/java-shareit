package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserStorage {
    User createUser(User newUser);

    User getUser(Long id);

    Collection<User> getAllUsers();

    void deleteUser(Long id);
}
