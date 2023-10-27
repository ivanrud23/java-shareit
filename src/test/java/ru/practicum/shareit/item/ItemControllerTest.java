package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemServiceImpl itemService;

    private final ItemDto itemDto = new ItemDto(
            1L,
            "Jon",
            "Desc of Item",
            true,
            1L
    );
    private final ItemDto itemDto2 = new ItemDto(
            4L,
            "Jon",
            "Desc of Item",
            true,
            4L
    );

    private final ItemResponseDto itemResponseDto = new ItemResponseDto(
            2L,
            "Donald",
            "Desc of Item",
            true,
            2L,
            null,
            null,
            new ArrayList<>()
    );
    private final ItemResponseDto itemResponseDto2 = new ItemResponseDto(
            3L,
            "Donald",
            "Desc of Item",
            true,
            3L,
            null,
            null,
            new ArrayList<>()
    );

    private final CommentDto commentDto = new CommentDto(
            1L,
            "dfsf",
            "Dfsd",
            LocalDateTime.of(2024, Month.APRIL, 8, 12, 30)
    );

    @Test
    @DirtiesContext
    void createItem() throws Exception {
        when(itemService.createItem(any(), any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class));

    }

    @Test
    @DirtiesContext
    void updateItem() throws Exception {
        when(itemService.updateItem(any(), any(), any()))
                .thenReturn(itemResponseDto);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemResponseDto.getId()), Long.class));
    }

    @Test
    @DirtiesContext
    void getItemByIdResponse() throws Exception {
        when(itemService.getItemById(any(), any()))
                .thenReturn(itemResponseDto);

        mvc.perform(get("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemResponseDto.getId()), Long.class));
    }

    @Test
    @DirtiesContext
    void getAllItemsWithBooking() throws Exception {
        List<ItemResponseDto> list = new ArrayList<>();
        list.add(itemResponseDto);
        list.add(itemResponseDto2);

        when(itemService.getAllItems(any(), any(), any()))
                .thenReturn(list);

        MvcResult result = mvc.perform(get("/items")
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
    void searchItem() throws Exception {
        List<ItemDto> list = new ArrayList<>();
        list.add(itemDto);
        list.add(itemDto2);

        when(itemService.searchItem(any(), any(), any()))
                .thenReturn(list);

        MvcResult result = mvc.perform(get("/items/search?text=fjkhgfdkjgh")
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
    void createComment() throws Exception {
        when(itemService.createComment(any(), any(), any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(commentDto.getText())));
    }
}