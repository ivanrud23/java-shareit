package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.ItemForBookingDto;
import ru.practicum.shareit.user.UserBookerDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingStatus status;

    private UserBookerDto booker;

    private ItemForBookingDto item;


}
