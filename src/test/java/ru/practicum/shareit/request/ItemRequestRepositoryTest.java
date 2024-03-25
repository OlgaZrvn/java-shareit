package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {

    private List<User> users = null;
    private List<Item> items = null;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository repository;

    @Test
    void testEmptyItemRequestByRequestorId() {
        List<ItemRequest> itemRequests = repository.findAllByRequestorId(1L);
        assertTrue(itemRequests.isEmpty());
    }

    @Test
    void testOneItemRequestByRequestorId() {
        User user = new User("user@ya.ru", "User1");
        userRepository.save(user);
        ItemRequest itemRequest = new ItemRequest( "Request1", user, LocalDateTime.now());
        repository.save(itemRequest);
        List<ItemRequest> itemRequests = repository.findAllByRequestorId(user.getId());
        assertEquals(1, itemRequests.size());
    }

    @Test
    void testEmptyItemRequest() {
        Page<ItemRequest> itemRequests = repository.findAllByRequestorIdNotOrderByCreatedDesc(1L,
                Pageable.ofSize(1));
        assertTrue(itemRequests.isEmpty());
    }

    @Test
    void testOneItemRequest() {
        User user = new User("user@ya.ru", "User1");
        ItemRequest itemRequest = new ItemRequest(1L, "Request1", user, LocalDateTime.now());
        itemRequest.setRequestor(user);
        repository.save(itemRequest);
        Page<ItemRequest> itemRequests = repository.findAllByRequestorIdNotOrderByCreatedDesc(1L,
                Pageable.ofSize(1));
        assertEquals(1, itemRequests.getSize());
    }
}