package ru.practicum.shareit.item;
/*
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
import ru.practicum.shareit.item.dto.ItemDto2;
import ru.practicum.shareit.item.dto.ItemResponse;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void shouldSaveNewItem() {
   /*     mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        ItemResponse itemResponse = generator.nextObject(ItemResponse.class);
        when(itemService.saveItem(Mockito.anyLong(), Mockito.any(ItemDto2.class))).thenReturn(itemResponse);

        mvc.perform(post("/items"))
                    .content(mapper.writeValueAsString(itemResponse))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-Sharer-User-Id", 0)
                .andExpect(status().isOk());

    */


  /*  ItemResponse item = generator.nextObject(ItemResponse.class);
    when(itemService.saveItem(Mockito.anyLong(), Mockito.any(ItemDto2.class)))
            .thenReturn(item);


        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(item))
            .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(item.getId()))
            .andExpect(jsonPath("$.name").value(item.getName()))
            .andExpect(jsonPath("$.description").value(item.getDescription()))
            .andExpect(jsonPath("$.available").value(item.getAvailable()))
            .andExpect(jsonPath("$.owner").value(item.getOwner()))
            .andExpect(jsonPath("$.lastBooking").value(item.getLastBooking()))
            .andExpect(jsonPath("$.nextBooking").value(item.getNextBooking()))
            .andExpect(jsonPath("$.request").value(item.getRequest()))
            .andExpect(jsonPath("$.comments").value(item.getComments()))
            .andExpect(jsonPath("$.requestId").value(item.getRequestId()));

   */
