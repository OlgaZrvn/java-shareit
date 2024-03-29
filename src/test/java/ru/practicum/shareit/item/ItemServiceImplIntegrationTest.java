package ru.practicum.shareit.item;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceImplIntegrationTest {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    ItemMapper itemMapper;

    private final EasyRandom generator = new EasyRandom();

    private final PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

    @Test
    void testGetAllItemsUser() {
        User owner = new User(0L, "user@ya.ru", "User1");
        Long id = userService.saveUser(owner).getId();
        Item item = generator.nextObject(Item.class);
        ItemResponse savedItem = itemService.saveItem(id, itemMapper.toItemDto2(item));
        savedItem.setOwner(owner);
        List<ItemResponse> items = itemService.getAllItemsUser(id,0,10);
        assertEquals(1, items.size());
    }

}