package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.AlreadyExistException;
import ru.practicum.shareit.exeption.NoDataException;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        return UserMapper.userToDto(repository.save(UserMapper.dtoToUSer(userDto)));

    }

    @Transactional
    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User oldUser = repository.findById(id).orElseThrow(() -> new NoDataException("Пользователь не существует"));
        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !oldUser.getEmail().equals(userDto.getEmail())) {
            if (repository.findAll().stream()
                    .anyMatch(user -> user.getEmail().equals(userDto.getEmail()))) {
                throw new AlreadyExistException("Пользователь с введенным mail уже существует");
            }
            oldUser.setEmail(userDto.getEmail());
        }
        return UserMapper.userToDto(repository.save(oldUser));
    }

    @Override
    public UserDto getUser(Long id) {
        return UserMapper.userToDto(repository.findById(id).orElseThrow(() -> new NoDataException("Пользователь не существует")));
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(UserMapper::userToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
