package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingRequestDto;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exeption.NoDataException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CommentRepository commentRepository;

    private final UserDto userDto = new UserDto(1L, "john.doe@mail.com", "John");
    private final ItemDto itemDto = new ItemDto(1L, "Item", "Desc item 1 текст", true, null);
    private final ItemResponseDto itemResponseDto = new ItemResponseDto(
            1L,
            "Item", "Desc item 1 текст",
            true,
            null,
            null,
            null,
            new ArrayList<>());

    @Test
    void createItem() {
        userService.createUser(userDto);
        ItemDto actualItem = itemService.createItem(itemDto, 1L);
        assertEquals(itemDto, actualItem);
    }

    @Test
    void createItem_emptyStatus() {
        userService.createUser(userDto);
        ItemDto itemDtoEmpty = new ItemDto();
        Exception exception = assertThrows(ValidationException.class, () -> {
            itemService.createItem(itemDtoEmpty, 1L);
        });
        assertEquals(exception.getMessage(), "Не задан статус");

    }

    @Test
    void createItem_emptyName() {
        userService.createUser(userDto);
        ItemDto itemDtoEmpty = new ItemDto();
        itemDtoEmpty.setAvailable(true);
        itemDtoEmpty.setName("");
        Exception exception = assertThrows(ValidationException.class, () -> {
            itemService.createItem(itemDtoEmpty, 1L);
        });
        assertEquals(exception.getMessage(), "Не задано имя");
    }

    @Test
    void createItem_emptyDesc() {
        userService.createUser(userDto);
        ItemDto itemDtoEmpty = new ItemDto();
        itemDtoEmpty.setAvailable(true);
        itemDtoEmpty.setName("Donald");
        Exception exception = assertThrows(ValidationException.class, () -> {
            itemService.createItem(itemDtoEmpty, 1L);
        });
        assertEquals(exception.getMessage(), "Не задано описание");
    }

    @Test
    void createItem_incorrectUser() {
        userService.createUser(userDto);
        ItemDto itemDtoEmpty = new ItemDto();
        itemDtoEmpty.setAvailable(true);
        itemDtoEmpty.setName("Donald");
        itemDtoEmpty.setDescription("");
        Exception exception = assertThrows(NoDataException.class, () -> {
            itemService.createItem(itemDtoEmpty, 10L);
        });
        assertEquals(exception.getMessage(), "Пользователь не существует");
    }

    @Test
    void updateItem() {
        userService.createUser(userDto);
        itemService.createItem(itemDto, 1L);
        ItemResponseDto actualItem = itemService.updateItem(1L, itemDto, 1L);
        assertEquals(itemResponseDto, actualItem);
    }

    @Test
    void getAllItems() {
        userService.createUser(userDto);
        itemService.createItem(itemDto, 1L);
        ItemDto itemDto2 = new ItemDto(2L, "Item_2", "Desc item 2", true, null);
        itemService.createItem(itemDto2, 1L);
        List<ItemResponseDto> actualResult = itemService.getAllItems(1L, 0, 10);
        assertEquals(actualResult.size(), 2);

    }

    @Test
    void getItemById() {
        userService.createUser(userDto);
        itemService.createItem(itemDto, 1L);
        ItemResponseDto actualResult = itemService.getItemById(1L, 1L);
        assertEquals(actualResult, itemResponseDto);
    }

    @Test
    void searchItem() {
        userService.createUser(userDto);
        itemService.createItem(itemDto, 1L);

        List<ItemDto> actualResult = itemService.searchItem("тЕкСт", 0, 10);
        assertEquals(actualResult.size(), 1);
    }

    @Test
    void createComment() throws InterruptedException {
        userService.createUser(userDto);
        UserDto userDto2 = new UserDto(2L, "donald.doe@mail.com", "John");
        itemService.createItem(itemDto, 1L);
        userService.createUser(userDto2);
        bookingService.createBooking(new BookingRequestDto(
                1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusMinutes(5)), 2L);

        Thread.sleep(2000);
        CommentDto commentDto = new CommentDto(1L, "My comment", "Jhon",
                LocalDateTime.now());

        CommentDto actualResult = itemService.createComment(1L, commentDto, 2L);
        assertEquals(actualResult.getId(), commentDto.getId());
    }
}