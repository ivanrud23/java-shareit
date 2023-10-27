package ru.practicum.shareit.request;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestServiceImpl itemRequestService;

    private final ItemRequestDtoResponse itemRequestDtoResponse = new ItemRequestDtoResponse(
            1L,
            "description",
            LocalDateTime.of(2014, Month.APRIL, 8, 12, 30),
            new ArrayList<>()

    );

    @Test
    @DirtiesContext
    void createItemRequest() throws Exception {
        when(itemRequestService.createItemRequest(any(), any()))
                .thenReturn(itemRequestDtoResponse);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDtoResponse))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDtoResponse.getDescription())))
                .andExpect(jsonPath("$.items", is(itemRequestDtoResponse.getItems())));
    }

    @Test
    @DirtiesContext
    void getItemRequest() throws Exception {
        ItemRequestDtoResponse itemRequestDtoResponse1 = new ItemRequestDtoResponse();
        List<ItemRequestDtoResponse> list = new ArrayList<>();
        list.add(itemRequestDtoResponse);
        list.add(itemRequestDtoResponse1);

        when(itemRequestService.getAllItemRequestByUser(any()))
                .thenReturn(list);

        MvcResult result = mvc.perform(get("/requests")
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
    void getItemRequestAnotherUser() throws Exception {
        ItemRequestDtoResponse itemRequestDtoResponse1 = new ItemRequestDtoResponse();
        List<ItemRequestDtoResponse> list = new ArrayList<>();
        list.add(itemRequestDtoResponse);
        list.add(itemRequestDtoResponse1);

        when(itemRequestService.getItemRequestByOtherUser(any(), any(), any()))
                .thenReturn(list);

        MvcResult result = mvc.perform(get("/requests/all")
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
    void getItemRequestById() throws Exception {

        when(itemRequestService.getItemRequestById(any(), any()))
                .thenReturn(itemRequestDtoResponse);

        mvc.perform(get("/requests/1")
                        .content(mapper.writeValueAsString(itemRequestDtoResponse))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDtoResponse.getDescription())))
                .andExpect(jsonPath("$.items", is(itemRequestDtoResponse.getItems())));
    }
}