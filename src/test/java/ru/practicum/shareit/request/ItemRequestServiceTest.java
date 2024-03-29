package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    private ItemRequestService itemRequestService;

    @Mock
    ItemRequestRepository repository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemRequestService = new ItemRequestServiceImpl(repository, userRepository, itemRepository);
    }

    @Test
    void testNotFindItemRequestById() {
        User user = new User("user@ya.ru", "User1");
        userRepository.save(user);
        assertThrows(NotFoundException.class, () ->  itemRequestService.getItemRequestById(0L, user.getId()));
    }

}