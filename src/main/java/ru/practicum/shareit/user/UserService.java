package ru.practicum.shareit.user;

import java.util.Collection;

interface UserService {
    UserDto createUser(UserDto newUser);

    UserDto updateUser(Long id, UserDto updateUser);

    UserDto getUser(Long id);

    Collection<UserDto> getAllUsers();

    void deleteUser(Long id);
}
