package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.AlreadyExistException;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorageImpl userStorage;

    public User createUser(User newUser) {
        if (userStorage.getUserStorage().values().stream()
                .anyMatch(user -> user.getEmail().equals(newUser.getEmail()))) {
            throw new AlreadyExistException("Пользователь с введенным mail уже существует");
        }
        return userStorage.createUser(newUser);
    }

    public User updateUser(Long id, User updateUser) {
        if (updateUser.getEmail() == null) {
            updateUser.setEmail(userStorage.getUserStorage().get(id).getEmail());
        }
        if (updateUser.getName() == null) {
            updateUser.setName(userStorage.getUserStorage().get(id).getName());
            if (!userStorage.getUserStorage().get(id).getEmail().equals(updateUser.getEmail())) {
                if (userStorage.getUserStorage().values().stream()
                        .anyMatch(user -> user.getEmail().equals(updateUser.getEmail()))) {
                    throw new AlreadyExistException("Пользователь с введенным mail уже существует");
                }
            }
        }

        return userStorage.updateUser(id, updateUser);
    }

    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }
}
