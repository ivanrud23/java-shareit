package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping("/bookings")
@Validated
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createBooking(@RequestBody @Valid BookingRequestDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long booker) {
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
                                                            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Не задан стартовый элемент") Integer from,
                                                            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Не задано количество выводимых элементов") Integer size) {
        return bookingService.getAllBookingByBookerId(state, bookerId, from, size);
    }


    @GetMapping("/owner")
    public List<BookingResponseDto> getAllBookingByItemOwnerId(@RequestParam(defaultValue = "ALL") String state,
                                                               @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                               @RequestParam(defaultValue = "0") @Min(value = 0, message = "Не задан стартовый элемент") Integer from,
                                                               @RequestParam(defaultValue = "10") @Min(value = 1, message = "Не задано количество выводимых элементов") Integer size) {
        return bookingService.getAllBookingByOwnerId(state, ownerId, from, size);
    }

}
