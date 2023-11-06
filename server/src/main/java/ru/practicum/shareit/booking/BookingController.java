package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createBooking(@RequestBody BookingRequestDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long booker) {
        return bookingService.createBooking(bookingDto, booker);

    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto confirmBooking(@PathVariable Long bookingId, @RequestParam Boolean approved,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.updateBookingApprove(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> getAllBookingByBookerId(@RequestParam(defaultValue = "ALL") String state,
                                                            @RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                            @RequestParam(defaultValue = "0") Integer from,
                                                            @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getAllBookingByBookerId(state, bookerId, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllBookingByItemOwnerId(@RequestParam(defaultValue = "ALL") String state,
                                                               @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                               @RequestParam(defaultValue = "0") Integer from,
                                                               @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getAllBookingByOwnerId(state, ownerId, from, size);
    }

}
