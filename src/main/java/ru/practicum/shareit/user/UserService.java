package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.AlreadyExistException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorageImpl userStorage;
    private final UserMapper userMapper;

    public UserDto createUser(UserDto newUser) {
        if (userStorage.getUserStorage().values().stream()
                .anyMatch(user -> user.getEmail().equals(newUser.getEmail()))) {
            throw new AlreadyExistException("Пользователь с введенным mail уже существует");
        }
        return userMapper.userToDto(userStorage.createUser(userMapper.dtoToUSer(newUser)));
    }

    public UserDto updateUser(Long id, UserDto updateUser) {
        User oldUser = userStorage.getUser(id);
        if (updateUser.getName() != null) {
            oldUser.setName(updateUser.getName());
        }
        if (updateUser.getEmail() != null) {
            if (!oldUser.getEmail().equals(updateUser.getEmail())) {
                if (userStorage.getUserStorage().values().stream()
                        .anyMatch(user -> user.getEmail().equals(updateUser.getEmail()))) {
                    throw new AlreadyExistException("Пользователь с введенным mail уже существует");
                }
            }
            oldUser.setEmail(updateUser.getEmail());
        }

        return userMapper.userToDto(oldUser);
    }

    public UserDto getUser(Long id) {
        return userMapper.userToDto(userStorage.getUser(id));
    }

    public Collection<UserDto> getAllUsers() {
        return userStorage.getAllUsers().stream()
                .map(userMapper::userToDto)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }
}
