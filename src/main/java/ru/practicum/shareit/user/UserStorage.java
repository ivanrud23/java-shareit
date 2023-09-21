package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserStorage {
    User createUser(User newUser);

    User updateUser(Long id, User updateUser);

    User getUser(Long id);

    Collection<User> getAllUsers();

    void deleteUser(Long id);
}
