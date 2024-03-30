package ru.practicum.shareit.item;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private final PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));


 /*   @Test
    void shouldGetOneItemsByUser() {
        User owner = generator.nextObject(User.class);
        Long id = userService.saveUser(owner).getId();
        Item item = generator.nextObject(Item.class);
        ItemResponse savedItem = itemService.saveItem(id, itemMapper.toItemDto2(item));
        savedItem.setOwner(owner);
        List<ItemResponse> items = itemService.getAllItemsUser(id,0,10);
        assertEquals(1, items.size());
    }

  */

    @Test
    void shouldNotSaveCommentWithoutItem() {
        User owner = generator.nextObject(User.class);
        Long id = userService.saveUser(owner).getId();
        CommentDto comment = generator.nextObject(CommentDto.class);
        assertThrows(NotFoundException.class, () ->  itemService.saveComment(id, comment, 0L));
    }

  /*  @Test
    void shouldNotSaveCommentWithoutUser() {
        User owner = generator.nextObject(User.class);
        Long userId = userService.saveUser(owner).getId();
        ItemDto2 item = generator.nextObject(ItemDto2.class);
        Long itemId = itemService.saveItem(userId, item).getId();
        CommentDto comment = generator.nextObject(CommentDto.class);
        assertThrows(NotFoundException.class, () ->  itemService.saveComment(5L, comment, itemId));
    }

    @Test
    void shouldNotSaveCommentWithoutBooking() {
        User owner = generator.nextObject(User.class);
        Long userId = userService.saveUser(owner).getId();
        ItemDto2 item = generator.nextObject(ItemDto2.class);
        Long itemId = itemService.saveItem(userId, item).getId();
        CommentDto comment = generator.nextObject(CommentDto.class);
        assertThrows(ValidationException.class, () ->  itemService.saveComment(userId, comment, itemId));
    }

  /*  @Test
    void shouldNotSaveCommentByOwner() {
        User owner = new User("owner@ya.ru", "Owner");
        Long ownerId = userService.saveUser(owner).getId();
        ItemDto2 item = generator.nextObject(ItemDto2.class);
        Long itemId = itemService.saveItem(ownerId, item).getId();
        CommentDto comment = generator.nextObject(CommentDto.class);
        User user = new User("user@ya.ru", "User");
        Long userId = userService.saveUser(user).getId();
        Booking booking = new Booking(0L, LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1),
                itemMapper.toItem(item), user);
        BookingDto bookingDto = bookingMapper.toBookingDto(booking);
        bookingDto.setItemId(itemId);
        bookingService.saveBooking(userId,  bookingDto);
        CommentResponse savedComment = itemService.saveComment(userId, comment, itemId);
        assertEquals(commentMapper.toCommentResponse(commentMapper.toComment(comment)), savedComment);
    }
  */
}