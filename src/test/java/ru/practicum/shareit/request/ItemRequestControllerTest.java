package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto2;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private final ItemRequestDto itemRequestDto = new ItemRequestDto(
            0L, "Description", 0L, LocalDateTime.now());

    private final EasyRandom generator = new EasyRandom();

    @Test
    public void shouldSaveNewItemRequest() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        when(itemRequestService.saveItemRequest(Mockito.anyLong(), Mockito.any(ItemRequestDto.class)))
                .thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requestorId", is(itemRequestDto.getId()), Long.class));
    }

    @Test
    public void shouldGetAllItemRequestsByUser() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<ItemRequestResponse> requests =  List.of(
                generator.nextObject(ItemRequestResponse.class),
                generator.nextObject(ItemRequestResponse.class));
        when(itemRequestService.getAllUserItemRequests(Mockito.anyLong()))
                .thenReturn(requests);
        mvc.perform(get("/requests/0")
                        .header("X-Sharer-User-Id", 0))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetAllItemRequests() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<ItemRequestResponse> requests =  List.of(
                generator.nextObject(ItemRequestResponse.class),
                generator.nextObject(ItemRequestResponse.class));
        when(itemRequestService.getAllItemRequests(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(requests);
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 0))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetItemRequestById() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<ItemDto2> items = List.of(generator.nextObject(ItemDto2.class), generator.nextObject(ItemDto2.class));
        ItemRequestResponse itemRequest = new ItemRequestResponse(
                0L, "Desc", 0L, LocalDateTime.now(), items);
        when(itemRequestService.getItemRequestById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemRequest);
        mvc.perform(get("/requests/0")
                        .header("X-Sharer-User-Id", 0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$.requestorId", is(itemRequest.getId()), Long.class));
    }
}