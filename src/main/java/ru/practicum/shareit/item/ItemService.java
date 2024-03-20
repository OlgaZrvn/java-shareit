package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentResponse;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item saveItem(Long userId, Item item);

    List<Item> getAllItemsUser(Long userId);

    Item getItemById(Long itemId, Long userId);

    Item updateItem(Long itemId, Long userId, Item item);

    List<Item> searchItems(String string);

    CommentResponse addComment(Long userId, CommentDto commentDto, Long itemId);
}
