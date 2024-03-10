package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private Long itemId = 1L;

    @Override
    public Item saveItem(User user, Item item) {
        item.setId(itemId++);
        item.setOwner(user);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getAllItemsUser(Long userId) {
        List<Item> itemsOfUser = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().getId() == userId) {
                itemsOfUser.add(item);
            }
        }
        return itemsOfUser;
    }

    @Override
    public Item getItemById(Long id) {
        return items.get(id);
    }

    @Override
    public Item updateItem(Long itemId, Item item) {
        item.setId(itemId);
        if (item.getName() != null) items.get(itemId).setName(item.getName());
        if (item.getDescription() != null) items.get(itemId).setDescription(item.getDescription());
        if (item.getAvailable() != null) items.get(itemId).setAvailable(item.getAvailable());
        return items.get(itemId);
    }

    @Override
    public List<Item> searchItems(String string) {
        String lowerCaseString = string.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getAvailable().equals(true))
                .filter(item -> (item.getName().toLowerCase().contains(lowerCaseString)) |
                        (item.getDescription().toLowerCase().contains(lowerCaseString)))
                .collect(Collectors.toList());
    }
}
