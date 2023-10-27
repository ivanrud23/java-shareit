package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.shareit.item.ItemForBookingDto;
import ru.practicum.shareit.user.UserBookerDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingServiceImpl bookingService;

    private final BookingResponseDto bookingResponseDto = new BookingResponseDto(
            1L,
            LocalDateTime.of(2024, Month.APRIL, 8, 12, 30),
            LocalDateTime.of(2024, Month.APRIL, 8, 13, 30),
            BookingStatus.WAITING,
            new UserBookerDto(2L),
            new ItemForBookingDto(1L, "name")
    );

    private final BookingResponseDto bookingResponseDto2 = new BookingResponseDto(
            2L,
            LocalDateTime.of(2024, Month.APRIL, 8, 12, 30),
            LocalDateTime.of(2024, Month.APRIL, 8, 13, 30),
            BookingStatus.WAITING,
            new UserBookerDto(2L),
            new ItemForBookingDto(1L, "name2")
    );

    @Test
    @DirtiesContext
    void createBooking() throws Exception {
        when(bookingService.createBooking(any(), any()))
                .thenReturn(bookingResponseDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingResponseDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId()), Long.class));

    }

    @Test
    @DirtiesContext
    void confirmRejectBooking() throws Exception {
        when(bookingService.updateBookingApprove(any(), any(), any()))
                .thenReturn(bookingResponseDto);

        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingResponseDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingResponseDto.getStatus().toString())));

    }

    @Test
    @DirtiesContext
    void getBookingById() throws Exception {
        when(bookingService.getBookingById(any(), any()))
                .thenReturn(bookingResponseDto);

        mvc.perform(get("/bookings/1")
                        .content(mapper.writeValueAsString(bookingResponseDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId()), Long.class));
    }

    @Test
    @DirtiesContext
    void getAllBookingByBookerId() throws Exception {
        List<BookingResponseDto> list = new ArrayList<>();
        list.add(bookingResponseDto);
        list.add(bookingResponseDto2);

        when(bookingService.getAllBookingByBookerId(any(), any(), any(), any()))
                .thenReturn(list);

        MvcResult result = mvc.perform(get("/bookings")
                        .content(mapper.writeValueAsString(list))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                .andExpect(status().isOk())
                .andReturn();
        assertEquals(mapper.readValue(result.getResponse().getContentAsString(), List.class).size(), 2);

    }


    @Test
    @DirtiesContext
    void getAllBookingByItemOwnerId() throws Exception {
        List<BookingResponseDto> list = new ArrayList<>();
        list.add(bookingResponseDto);
        list.add(bookingResponseDto2);

        when(bookingService.getAllBookingByOwnerId(any(), any(), any(), any()))
                .thenReturn(list);

        MvcResult result = mvc.perform(get("/bookings/owner")
                        .content(mapper.writeValueAsString(list))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                .andExpect(status().isOk())
                .andReturn();
        assertEquals(mapper.readValue(result.getResponse().getContentAsString(), List.class).size(), 2);

    }
}