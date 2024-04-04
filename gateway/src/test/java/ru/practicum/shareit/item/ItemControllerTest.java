package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    private ObjectMapper mapper;

    @MockBean
    private ItemClient itemClient;

    @Autowired
    private MockMvc mvc;

    private final EasyRandom generator = new EasyRandom();

    @Test
    public void shouldNotSaveItemWithoutName() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        ItemDto itemDto = generator.nextObject(ItemDto.class);
        itemDto.setName(null);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotSaveItemWithoutDescription() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        ItemDto itemDto = generator.nextObject(ItemDto.class);
        itemDto.setDescription(null);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotSaveItemWithoutStatus() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        ItemDto itemDto = generator.nextObject(ItemDto.class);
        itemDto.setAvailable(null);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotSaveItemWithoutHeader() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        ItemDto itemDto = generator.nextObject(ItemDto.class);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotUpdateItemWithoutHeader() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        ItemDto itemDto = generator.nextObject(ItemDto.class);
        mvc.perform(patch("/items/0")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotUpdateItemWithHeaderNull() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        ItemDto itemDto = generator.nextObject(ItemDto.class);
        mvc.perform(patch("/items/0")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "null"))
                .andExpect(status().isBadRequest());
    }

}