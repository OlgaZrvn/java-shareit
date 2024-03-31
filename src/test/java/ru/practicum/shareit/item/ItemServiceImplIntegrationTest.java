package ru.practicum.shareit.item;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

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
    CommentMapper commentMapper;

    @Autowired
    BookingMapper bookingMapper;

    private final EasyRandom generator = new EasyRandom();

    @Test
    void shouldNotSaveCommentWithoutItem() {
        User owner = generator.nextObject(User.class);
        Long id = userService.saveUser(owner).getId();
        CommentDto comment = generator.nextObject(CommentDto.class);
        assertThrows(NotFoundException.class, () ->  itemService.saveComment(id, comment, 0L));
    }

}