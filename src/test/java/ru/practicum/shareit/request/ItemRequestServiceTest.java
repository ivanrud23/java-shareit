package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ItemRequestServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemRequestService itemRequestService;

    private final UserDto userDto = new UserDto(1L, "john.doe@mail.com", "John");
    private final UserDto userDto2 = new UserDto(2L, "donald.doe@mail.com", "Donald");
    private final ItemRequestDto itemRequestDto = new ItemRequestDto(
            "Give me please something",
            LocalDateTime.of(2014, Month.APRIL, 8, 12, 30));
    private final ItemRequestDtoResponse itemRequestDtoResponse = new ItemRequestDtoResponse(
            1L,
            "Give me please something",
            LocalDateTime.of(2014, Month.APRIL, 8, 12, 30),
            new ArrayList<>());


    @Test
    @DirtiesContext
    void createItemRequest() {
        userService.createUser(userDto);
        ItemRequestDtoResponse actualResult = itemRequestService.createItemRequest(itemRequestDto, 1L);
        assertEquals(actualResult.getId(), itemRequestDtoResponse.getId());
    }

    @Test
    @DirtiesContext
    void getAllItemRequestByUser() {
        userService.createUser(userDto);
        itemRequestService.createItemRequest(itemRequestDto, 1L);
        ItemRequestDto itemRequestDto2 = new ItemRequestDto(
                "Give me please something2",
                LocalDateTime.of(2014, Month.APRIL, 8, 12, 30));
        itemRequestService.createItemRequest(itemRequestDto2, 1L);
        List<ItemRequestDtoResponse> actualResult = itemRequestService.getAllItemRequestByUser(1L);
        assertEquals(actualResult.size(), 2);

    }

    @Test
    @DirtiesContext
    void getItemRequestByOtherUser() {
        userService.createUser(userDto);
        userService.createUser(userDto2);
        itemRequestService.createItemRequest(itemRequestDto, 1L);
        ItemRequestDto itemRequestDto2 = new ItemRequestDto(
                "Give me please something2",
                LocalDateTime.of(2014, Month.APRIL, 8, 12, 30));
        itemRequestService.createItemRequest(itemRequestDto2, 1L);
        List<ItemRequestDtoResponse> actualResult = itemRequestService.getItemRequestByOtherUser(2L, 0, 10);
        assertEquals(actualResult.size(), 2);
    }

    @Test
    @DirtiesContext
    void getItemRequestByOtherUser_wrongPagination() {
        userService.createUser(userDto);
        userService.createUser(userDto2);
        itemRequestService.createItemRequest(itemRequestDto, 1L);
        ItemRequestDto itemRequestDto2 = new ItemRequestDto(
                "Give me please something2",
                LocalDateTime.of(2014, Month.APRIL, 8, 12, 30));
        itemRequestService.createItemRequest(itemRequestDto2, 1L);
        Exception exception = assertThrows(ValidationException.class, () -> {
            itemRequestService.getItemRequestByOtherUser(2L, 0, 0);
        });
        assertEquals(exception.getMessage(), "Не задан стартовый элемент или количество выводимых элементов");
    }

    @Test
    @DirtiesContext
    void getItemRequestById() {
        userService.createUser(userDto);
        itemRequestService.createItemRequest(itemRequestDto, 1L);
        ItemRequestDtoResponse actualResult = itemRequestService.getItemRequestById(1L, 1L);
        assertEquals(actualResult.getId(), itemRequestDtoResponse.getId());
    }
}