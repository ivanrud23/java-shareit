package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exeption.NoDataException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemForBookingDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserBookerDto;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    private final UserDto userDto = new UserDto(1L, "john.doe@mail.com", "John");
    private final UserDto userDto2 = new UserDto(2L, "donald.doe@mail.com", "Donald");
    private final ItemDto itemDto = new ItemDto(1L, "Item", "Desc item 1 текст", true, null);
    private final BookingResponseDto bookingResponseDto = new BookingResponseDto(
            1L,
            LocalDateTime.now().plusSeconds(1),
            LocalDateTime.now().plusMinutes(5),
            BookingStatus.WAITING,
            new UserBookerDto(),
            new ItemForBookingDto());

    @Test
    @DirtiesContext
    void createBooking() {
        userService.createUser(userDto);
        userService.createUser(userDto2);
        itemService.createItem(itemDto, 1L);
        BookingResponseDto actualResult = bookingService.createBooking(new BookingRequestDto(
                1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusMinutes(5)), 2L);
        assertEquals(actualResult.getId(), bookingResponseDto.getId());
    }

    @Test
    @DirtiesContext
    void createBooking_wrongUser() {
        userService.createUser(userDto);
        userService.createUser(userDto2);
        itemService.createItem(itemDto, 1L);
        Exception exception = assertThrows(NoDataException.class, () -> {
            bookingService.createBooking(new BookingRequestDto(
                    1L,
                    LocalDateTime.now().plusSeconds(1),
                    LocalDateTime.now().plusMinutes(5)), 1L);
        });
        assertEquals(exception.getMessage(), "Букер не может быть владельцем");
    }

    @Test
    @DirtiesContext
    void createBooking_notAvailable() {
        userService.createUser(userDto);
        userService.createUser(userDto2);
        itemDto.setAvailable(false);
        itemService.createItem(itemDto, 1L);
        Exception exception = assertThrows(ValidationException.class, () -> {
            bookingService.createBooking(new BookingRequestDto(
                    1L,
                    LocalDateTime.now().plusSeconds(1),
                    LocalDateTime.now().plusMinutes(5)), 2L);
        });
        assertEquals(exception.getMessage(), "Вещь недоступна");
    }

    @Test
    @DirtiesContext
    void updateBookingApprove() {
        userService.createUser(userDto);
        userService.createUser(userDto2);
        itemService.createItem(itemDto, 1L);
        bookingService.createBooking(new BookingRequestDto(
                1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusMinutes(5)), 2L);
        BookingResponseDto actualResult = bookingService.updateBookingApprove(1L, true, 1L);
        assertEquals(actualResult.getStatus(), BookingStatus.APPROVED);
    }

    @Test
    @DirtiesContext
    void updateBookingApprove_userNotOwner() {
        userService.createUser(userDto);
        userService.createUser(userDto2);
        itemService.createItem(itemDto, 1L);
        bookingService.createBooking(new BookingRequestDto(
                1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusMinutes(5)), 2L);
        Exception exception = assertThrows(NoDataException.class, () -> {
            bookingService.updateBookingApprove(1L, true, 2L);
        });
        assertEquals(exception.getMessage(), "Только владелец может обновлять статус бронирования");
    }

    @Test
    @DirtiesContext
    void getBookingById() {
        userService.createUser(userDto);
        userService.createUser(userDto2);
        itemService.createItem(itemDto, 1L);
        bookingService.createBooking(new BookingRequestDto(
                1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusMinutes(5)), 2L);
        BookingResponseDto actualResult = bookingService.getBookingById(1L, 1L);
        assertEquals(actualResult.getId(), bookingResponseDto.getId());
    }

    @Test
    @DirtiesContext
    void getBookingByItemId() {
        userService.createUser(userDto);
        userService.createUser(userDto2);
        itemService.createItem(itemDto, 1L);
        bookingService.createBooking(new BookingRequestDto(
                1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusMinutes(5)), 2L);
        bookingService.createBooking(new BookingRequestDto(
                1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusMinutes(5)), 2L);
        List<BookingResponseDto> actualResult = bookingService.getBookingByItemId(1L, 1L);
        assertEquals(actualResult.size(), 2);
    }

    @Test
    @DirtiesContext
    void getAllBookingByBookerId() {
        userService.createUser(userDto);
        userService.createUser(userDto2);
        itemService.createItem(itemDto, 1L);
        bookingService.createBooking(new BookingRequestDto(
                1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusMinutes(5)), 2L);
        bookingService.createBooking(new BookingRequestDto(
                1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusMinutes(5)), 2L);
        List<BookingResponseDto> actualResult = bookingService.getAllBookingByBookerId("ALL", 2L, 0, 10);
        assertEquals(actualResult.size(), 2);
    }

    @Test
    @DirtiesContext
    void getAllBookingByOwnerId() {
        userService.createUser(userDto);
        userService.createUser(userDto2);
        itemService.createItem(itemDto, 1L);
        bookingService.createBooking(new BookingRequestDto(
                1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusMinutes(5)), 2L);
        bookingService.createBooking(new BookingRequestDto(
                1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusMinutes(5)), 2L);
        List<BookingResponseDto> actualResult = bookingService.getAllBookingByOwnerId("ALL", 1L, 0, 10);
        assertEquals(actualResult.size(), 2);
    }
}