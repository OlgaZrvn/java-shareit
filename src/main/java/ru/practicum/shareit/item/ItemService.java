package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item saveItem(Long userId, Item item);
    List<Item> getAllItemsUser(Long userId);
    Item getItemById(Long id);
    Item updateItem(Long itemId, Long userId, Item item);
    List<Item> searchItems(String string);
}
