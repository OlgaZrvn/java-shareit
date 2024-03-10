package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRepository {

    Item saveItem(User user, Item item);

    List<Item> getAllItemsUser(Long userId);

    Item getItemById(Long id);

    Item updateItem(Long itemId, Item item);

    List<Item> searchItems(String string);
}
