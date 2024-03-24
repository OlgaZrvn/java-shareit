package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemResponse saveItem(Long userId, Item item);

    List<ItemResponse> getAllItemsUser(Long userId);

    ItemResponse getItemById(Long itemId, Long userId);

    ItemResponse updateItem(Long itemId, Long userId, Item item);

    List<Item> searchItems(String string);

    CommentResponse saveComment(Long userId, CommentDto commentDto, Long itemId);
}
