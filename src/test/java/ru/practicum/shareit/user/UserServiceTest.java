package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private final UserDto userDto = new UserDto(1L, "john.doe@mail.com", "John");
    private final UserDto updateUserDto = new UserDto(1L, "Update_email", "Update_name");

    @Test
    void createUser() {
        UserDto actualUser = userService.createUser(userDto);
        assertEquals(userDto, actualUser);
    }

    @Test
    void updateUser() {
        userService.createUser(userDto);
        UserDto actualUser = userService.updateUser(1L, updateUserDto);

        assertEquals(new UserDto(1L, "Update_email", "Update_name"), actualUser);
    }

    @Test
    void getUser() {
        userService.createUser(userDto);
        UserDto actualUser = userService.getUser(1L);

        assertEquals(userDto, actualUser);
    }

    @Test
    void getAllUsers() {
        UserDto userDto2 = new UserDto(
                2L,
                "john.doe@mail.com",
                "John");
        userService.createUser(userDto2);

        UserDto userDto3 = new UserDto(
                3L,
                "donald.doe@mail.com",
                "Donald");
        userService.createUser(userDto3);

        List<UserDto> list = userService.getAllUsers();

        assertEquals(list.size(), 2);
    }

    @Test
    void deleteUser() {
        UserDto userDto2 = new UserDto(
                2L,
                "john.doe@mail.com",
                "John");
        userService.createUser(userDto2);
        userService.deleteUser(1L);

        List<UserDto> list = userService.getAllUsers();

        assertEquals(list.size(), 0);
    }
}