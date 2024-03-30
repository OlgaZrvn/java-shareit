package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void testEmptyItemByOwnerId() {
        User user = new User("User", "user@ya.ru");
        userRepository.save(user);
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        List<Item> items = itemRepository.findByOwnerId(user.getId(), page).toList();
        assertTrue(items.isEmpty());
    }

    @Test
    void testFindOneItemByOwnerId() {
        User user = new User("user@ya.ru", "User1");
        userRepository.save(user);
        Item item = new Item("Item1", "Desc1", true, user);
        itemRepository.save(item);
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        List<Item> items = itemRepository.findByOwnerId(user.getId(), page).toList();
        assertEquals(1, items.size());
    }

    @Test
    void testNotFindItemByRequestId() {
        User user = new User("user@ya.ru", "User1");
        userRepository.save(user);
        ItemRequest request1 = new ItemRequest("Request1", user, LocalDateTime.now());
        itemRequestRepository.save(request1);
        ItemRequest request2 = new ItemRequest("Request2", user, LocalDateTime.now());
        itemRequestRepository.save(request2);
        Item item = new Item(0L, "Item1", "Desc1", true, user, request1);
        itemRepository.save(item);
        List<Item> items = itemRepository.findByRequestId(request2.getId());
        assertEquals(0, items.size());
    }

    @Test
    void testFindOneItemByRequestId() {
        User user = new User("user@ya.ru", "User1");
        userRepository.save(user);
        ItemRequest request = new ItemRequest("Request1", user, LocalDateTime.now());
        itemRequestRepository.save(request);
        Item item = new Item(0L, "Item1", "Desc1", true, user, request);
        itemRepository.save(item);
        List<Item> items = itemRepository.findByRequestId(request.getId());
        assertEquals(1, items.size());
    }

 /*   @Test
    void findByRequestIdIn() {
        User user = new User("user@ya.ru", "User1");
        userRepository.save(user);
        ItemRequest request = new ItemRequest("Request1", user, LocalDateTime.now());
        itemRequestRepository.save(request);
        Item item = new Item(0L, "Item1", "Desc1", true, user, request);
        itemRepository.save(item);
        List<Item> items = itemRepository.findByRequestId(request.getId());
        assertEquals(1, items.size());
    }

  */

    @Test
    void searchItems() {
        User user = new User("user@ya.ru", "User1");
        userRepository.save(user);
        Item item1 = new Item("Item1", "Desc1", true, user);
        itemRepository.save(item1);
        Item item2 = new Item("Item2", "Good item", true, user);
        itemRepository.save(item2);
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        List<Item> items = itemRepository.searchItems("GOOD", page).toList();
        assertEquals(1, items.size());
    }
}