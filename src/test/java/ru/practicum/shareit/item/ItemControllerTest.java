package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemMapper itemMapper;

    @Autowired
    private MockMvc mvc;

    private final EasyRandom generator = new EasyRandom();


    @Test
    public void shouldSaveNewItem() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        ItemResponse itemResponse = generator.nextObject(ItemResponse.class);
        when(itemService.saveItem(Mockito.anyLong(), Mockito.any(ItemDto2.class))).thenReturn(itemResponse);
        Item item = ItemMapperNew.toItem(itemResponse);
        ItemDto2 itemDto = ItemMapperNew.toItemDto(item);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemResponse.getId()))
                .andExpect(jsonPath("$.name").value(itemResponse.getName()))
                .andExpect(jsonPath("$.description").value(itemResponse.getDescription()))
                .andExpect(jsonPath("$.available").value(itemResponse.getAvailable()))
                .andExpect(jsonPath("$.owner").value(itemResponse.getOwner()));
    }

    @Test
    public void shouldUpdateItem() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        ItemResponse itemResponse = generator.nextObject(ItemResponse.class);
        itemResponse.setId(0L);
        when(itemService.updateItem(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Item.class)))
                .thenReturn(itemResponse);
        Item item = ItemMapperNew.toItem(itemResponse);
        ItemDto2 itemDto = ItemMapperNew.toItemDto(item);
        mvc.perform(patch("/items/0")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 0))
                .andExpect(status().isOk());
           /*     .andExpect(jsonPath("$.id").value(itemResponse.getId()))
                .andExpect(jsonPath("$.name").value(itemResponse.getName()))
                .andExpect(jsonPath("$.description").value(itemResponse.getDescription()))
                .andExpect(jsonPath("$.available").value(itemResponse.getAvailable()))
                .andExpect(jsonPath("$.owner").value(itemResponse.getOwner()))
                .andExpect(jsonPath("$.lastBooking").value(itemResponse.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(itemResponse.getNextBooking()))
                .andExpect(jsonPath("$.request").value(itemResponse.getRequest()))
                .andExpect(jsonPath("$.comments").value(itemResponse.getComments()))
                .andExpect(jsonPath("$.requestId").value(itemResponse.getRequestId()));

            */
    }
}