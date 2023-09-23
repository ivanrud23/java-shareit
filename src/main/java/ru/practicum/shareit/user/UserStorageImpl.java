package ru.practicum.shareit.user;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> userStorage = new HashMap<>();
    private volatile int idCounter = 1;

    @Override
    public User createUser(User newUser) {
        newUser.setId(counter());
        userStorage.put(newUser.getId(), newUser);
        return newUser;
    }


    @Override
    public User getUser(Long id) {
        return userStorage.get(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userStorage.values();
    }

    @Override
    public void deleteUser(Long id) {
        userStorage.remove(id);
    }


    public int counter() {
        return idCounter++;
    }
}
