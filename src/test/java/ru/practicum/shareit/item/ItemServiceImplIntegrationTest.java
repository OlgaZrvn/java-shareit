package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceImplIntegrationTest {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    BookingService bookingService;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    BookingMapper bookingMapper;

    @Test
    void shouldNotSaveItemWithoutUser() {
        assertThrows(NotFoundException.class, () ->  itemService.saveItem(0L, new ItemDto()));
    }
}