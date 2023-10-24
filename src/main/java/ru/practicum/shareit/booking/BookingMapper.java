package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingResponseDto mapToBookingResponseDto(Booking booking) {

        return new BookingResponseDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                UserMapper.userToBookerDto(booking.getBooker()),
                ItemMapper.mapForBookingDto(booking.getItem())
        );
    }


    public static Booking mapToBooking(BookingRequestDto bookingDto, User booker, Item item) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }

    public static BookingItemDto mapToBookerItemDto(Booking booking) {
        return new BookingItemDto(
                booking.getId(),
                booking.getBooker().getId()
        );
    }

}
