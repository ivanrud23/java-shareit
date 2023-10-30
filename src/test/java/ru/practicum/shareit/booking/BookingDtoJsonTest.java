package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.ItemForBookingDto;
import ru.practicum.shareit.user.UserBookerDto;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingResponseDto> json;

    @Test
    void testBookingDto() throws Exception {
        BookingResponseDto bookingResponseDto = new BookingResponseDto(
                1L,
                LocalDateTime.of(2014, Month.APRIL, 8, 12, 30),
                LocalDateTime.of(2014, Month.APRIL, 8, 12, 31),
                BookingStatus.APPROVED,
                new UserBookerDto(),
                new ItemForBookingDto());

        JsonContent<BookingResponseDto> result = json.write(bookingResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2014-04-08T12:30:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2014-04-08T12:31:00");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");

    }
}
