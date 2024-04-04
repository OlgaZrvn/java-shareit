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
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentMapperNew;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
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

    @Autowired
    private MockMvc mvc;

    private final EasyRandom generator = new EasyRandom();


    @Test
    public void shouldSaveNewItem() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        ItemResponse itemResponse = generator.nextObject(ItemResponse.class);
        when(itemService.saveItem(Mockito.anyLong(), Mockito.any(ItemDto.class))).thenReturn(itemResponse);
        Item item = ItemMapperNew.toItem(itemResponse);
        ItemDto itemDto = ItemMapperNew.toItemDto(item);
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
        ItemDto itemDto = ItemMapperNew.toItemDto(item);
        mvc.perform(patch("/items/0")
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
    public void shouldGetItemById() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        ItemResponse itemResponse = generator.nextObject(ItemResponse.class);
        itemResponse.setId(0L);
        when(itemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemResponse);
        mvc.perform(get("/items/0")
                        .header("X-Sharer-User-Id", 0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemResponse.getId()))
                .andExpect(jsonPath("$.name").value(itemResponse.getName()))
                .andExpect(jsonPath("$.description").value(itemResponse.getDescription()))
                .andExpect(jsonPath("$.available").value(itemResponse.getAvailable()))
                .andExpect(jsonPath("$.owner").value(itemResponse.getOwner()));
    }

    @Test
    public void shouldGetAllItem() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());

        List<ItemResponse> itemResponses = List.of(
                generator.nextObject(ItemResponse.class),
                generator.nextObject(ItemResponse.class));

        when(itemService.getAllItemsUser(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(itemResponses);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 0))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetItemsSearched() throws Exception {
        List<Item> items = List.of(
                generator.nextObject(Item.class),
                generator.nextObject(Item.class));

        when(itemService.searchItems(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(items);

        mvc.perform(get("/items/search")
                        .param("text", "text")
                        .param("from", "0")
                        .param("size", "20")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void shouldSaveNewComment() throws Exception {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        CommentResponse commentResponse = generator.nextObject(CommentResponse.class);
        when(itemService.saveComment(Mockito.anyLong(), Mockito.any(CommentDto.class), Mockito.anyLong()))
                .thenReturn(commentResponse);
        Comment comment = CommentMapperNew.toComment(commentResponse);
        CommentDto commentDto = CommentMapperNew.toCommentDto(comment);

        mvc.perform(post("/items/0/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentResponse.getId()))
                .andExpect(jsonPath("$.text").value(commentResponse.getText()))
                .andExpect(jsonPath("$.authorName").value(commentResponse.getAuthorName()));
    }
}