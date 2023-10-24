package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {

    BookingResponseDto createBooking(BookingRequestDto bookingDto, Long ownerId);

    BookingResponseDto updateBookingApprove(Long bookingId, Boolean approved, Long userId);

    BookingResponseDto getBookingById(Long bookingId, Long userId);

    List<BookingResponseDto> getBookingByItemId(Long itemId, Long userId);

    List<BookingResponseDto> getAllBookingByBookerId(String state, Long ownerId);

    List<BookingResponseDto> getAllBookingByOwnerId(String state, Long ownerId);
}
