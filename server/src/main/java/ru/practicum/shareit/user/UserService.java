package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto newUser);

    UserDto updateUser(Long id, UserDto updateUser);

    UserDto getUser(Long id);

    List<UserDto> getAllUsers();

    void deleteUser(Long id);
}
