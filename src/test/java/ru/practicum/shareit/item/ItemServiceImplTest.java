package ru.practicum.shareit.item;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentResponse;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    private ItemServiceImpl itemService;

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    ItemRequestRepository itemRequestRepository;

    ItemMapper itemMapper = new ItemMapperImpl();

    private final User user = new User(0L, "User", "user@ya.ru");
    private final EasyRandom generator = new EasyRandom();

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository, bookingRepository,
                itemRequestRepository, itemMapper);
    }

    @Test
    void shouldNotSaveItemWithNoOwner() {
        ItemDto item = generator.nextObject(ItemDto.class);
        assertThrows(NotFoundException.class, () ->  itemService.saveItem(0L, item));
    }

    @Test
    void shouldSaveOneItem() {
        User owner = new User("user@ya.ru", "User1");
        owner.setId(0L);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));
        ItemDto itemDto = generator.nextObject(ItemDto.class);
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(owner);
        when(itemRepository.save(Mockito.any())).thenReturn(item);
        ItemResponse itemResponse = itemMapper.toItemResponse(itemRepository.save(item));
        if (itemDto.getRequestId() != null) {
            itemResponse.setRequestId(itemDto.getRequestId());
        }
        ItemResponse returnedItem = itemService.saveItem(0L, itemDto);
        assertEquals(itemResponse, returnedItem);
    }

    @Test
    void shouldGet2Items() {
        User owner = new User(0L, "user@ya.ru", "User1");
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));

        Item item1 = generator.nextObject(Item.class);
        Item item2 = generator.nextObject(Item.class);
        Page<Item> itemsPage = new PageImpl<>(List.of(item1, item2));
        when(itemRepository.findByOwnerId(Mockito.anyLong(), Mockito.any())).thenReturn(itemsPage);

        Item item = generator.nextObject(Item.class);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        ItemResponse itemResponse = new ItemResponse(
                item.getId(), item.getName(), item.getDescription(), item.getAvailable(), user.getId());
        itemResponse.setOwner(item.getOwner());
        List<Comment> commentList = List.of(
                generator.nextObject(Comment.class),
                generator.nextObject(Comment.class)
        );
        when(commentRepository.findByItemId(Mockito.anyLong())).thenReturn(commentList);

        List<ItemResponse> savedItems = itemService.getAllItemsUser(0L, 0, 10);
        assertEquals(2, savedItems.size());
    }


    @Test
    void shouldNotFindItemByIdWithoutItem() {
        assertThrows(NotFoundException.class, () ->  itemService.getItemById(0L, 0L));
    }

    @Test
    void shouldNotFindItemByIdWithoutUser() {
        Item item = generator.nextObject(Item.class);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        assertThrows(NotFoundException.class, () ->  itemService.getItemById(item.getId(), 0L));
    }

    @Test
    void shouldFindItemById() {
        Item item = generator.nextObject(Item.class);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        ItemResponse itemResponse = new ItemResponse(
                item.getId(), item.getName(), item.getDescription(), item.getAvailable(), user.getId());
        itemResponse.setOwner(item.getOwner());
        List<Comment> commentList = List.of(
                generator.nextObject(Comment.class),
                generator.nextObject(Comment.class)
        );
        when(commentRepository.findByItemId(Mockito.anyLong())).thenReturn(commentList);

        ItemResponse returnedItem = itemService.getItemById(item.getId(), user.getId());
        assertEquals(itemResponse.getId(), returnedItem.getId());
    }

    @Test
    void shouldFindItemByIdWithoutLastAndNextBooking() {
        Item item = generator.nextObject(Item.class);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        ItemResponse itemResponse = new ItemResponse(
                item.getId(), item.getName(), item.getDescription(), item.getAvailable(), user.getId());
        itemResponse.setOwner(user);

        List<Comment> commentList = List.of(
                generator.nextObject(Comment.class),
                generator.nextObject(Comment.class)
        );
        when(commentRepository.findByItemId(Mockito.anyLong())).thenReturn(commentList);

        ItemResponse returnedItem = itemService.getItemById(item.getId(), user.getId());
        assertEquals(itemResponse.getId(), returnedItem.getId());
    }

    @Test
    void shouldNotUpdateItemWithoutUser() {
        Item item = generator.nextObject(Item.class);
        assertThrows(NotFoundException.class, () ->  itemService.updateItem(item.getId(), 0L, item));
    }

    @Test
    void shouldNotUpdateItemWithUserNotOwner() {
        Item item = generator.nextObject(Item.class);
        User owner = new User("owner@ya.ru", "Owner1");
        owner.setId(0L);
        item.setOwner(owner);
        User user = new User("user@ya.ru", "User");
        user.setId(1L);
        when(itemRepository.getReferenceById(Mockito.anyLong())).thenReturn(item);
        when(userRepository.getReferenceById(Mockito.anyLong())).thenReturn(user);
        assertThrows(NotFoundException.class, () ->  itemService.checkOwner(item.getId(), user.getId()));
    }

    @Test
    void shouldUpdateItem() {
        Item item = generator.nextObject(Item.class);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        User owner = new User("owner@ya.ru", "Owner1");
        owner.setId(0L);
        item.setOwner(owner);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));
        ItemResponse updatedItem = itemService.getItemById(item.getId(), owner.getId());
        when(itemRepository.save(Mockito.any())).thenReturn(itemMapper.toItem(updatedItem));
        when(itemRepository.getReferenceById(Mockito.anyLong())).thenReturn(item);
        when(userRepository.getReferenceById(Mockito.anyLong())).thenReturn(owner);
        ItemResponse returnedItem = itemService.updateItem(item.getId(), owner.getId(),itemMapper.toItem(updatedItem));
        assertEquals(updatedItem, returnedItem);
    }

    @Test
    void shouldUpdateItemWithoutName() {
        Item item = generator.nextObject(Item.class);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        User owner = new User("owner@ya.ru", "Owner1");
        owner.setId(0L);
        item.setOwner(owner);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));
        ItemResponse updatedItem = itemService.getItemById(item.getId(), owner.getId());
        updatedItem.setName(null);
        when(itemRepository.save(Mockito.any())).thenReturn(itemMapper.toItem(updatedItem));
        when(itemRepository.getReferenceById(Mockito.anyLong())).thenReturn(item);
        when(userRepository.getReferenceById(Mockito.anyLong())).thenReturn(owner);
        ItemResponse returnedItem = itemService.updateItem(item.getId(), owner.getId(),itemMapper.toItem(updatedItem));
        updatedItem.setName(returnedItem.getName());
        assertEquals(updatedItem, returnedItem);
    }

    @Test
    void shouldUpdateItemWithoutDescription() {
        Item item = generator.nextObject(Item.class);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        User owner = new User("owner@ya.ru", "Owner1");
        owner.setId(0L);
        item.setOwner(owner);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));
        ItemResponse updatedItem = itemService.getItemById(item.getId(), owner.getId());
        updatedItem.setDescription(null);
        when(itemRepository.save(Mockito.any())).thenReturn(itemMapper.toItem(updatedItem));
        when(itemRepository.getReferenceById(Mockito.anyLong())).thenReturn(item);
        when(userRepository.getReferenceById(Mockito.anyLong())).thenReturn(owner);
        ItemResponse returnedItem = itemService.updateItem(item.getId(), owner.getId(),itemMapper.toItem(updatedItem));
        updatedItem.setDescription(returnedItem.getDescription());
        assertEquals(updatedItem, returnedItem);
    }

    @Test
    void shouldUpdateItemWithoutAvailable() {
        Item item = generator.nextObject(Item.class);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        User owner = new User("owner@ya.ru", "Owner1");
        owner.setId(0L);
        item.setOwner(owner);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));
        ItemResponse updatedItem = itemService.getItemById(item.getId(), owner.getId());
        updatedItem.setAvailable(null);
        when(itemRepository.save(Mockito.any())).thenReturn(itemMapper.toItem(updatedItem));
        when(itemRepository.getReferenceById(Mockito.anyLong())).thenReturn(item);
        when(userRepository.getReferenceById(Mockito.anyLong())).thenReturn(owner);
        ItemResponse returnedItem = itemService.updateItem(item.getId(), owner.getId(),itemMapper.toItem(updatedItem));
        updatedItem.setAvailable(returnedItem.getAvailable());
        assertEquals(updatedItem, returnedItem);
    }

    @Test
    void shouldNotSaveCommentWithoutItem() {
        CommentDto commentDto = generator.nextObject(CommentDto.class);
        assertThrows(NotFoundException.class, () -> itemService.saveComment(user.getId(), commentDto, 0L));
    }

    @Test
    void shouldNotSaveCommentWithoutUser() {
        CommentDto commentDto = generator.nextObject(CommentDto.class);
        Item item = generator.nextObject(Item.class);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        assertThrows(NotFoundException.class, () -> itemService.saveComment(user.getId(), commentDto, item.getId()));
    }

    @Test
    void shouldNotSaveCommentWithoutBooking() {
        CommentDto commentDto = generator.nextObject(CommentDto.class);
        Item item = generator.nextObject(Item.class);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        assertThrows(ValidationException.class, () -> itemService.saveComment(user.getId(), commentDto, item.getId()));
    }

    @Test
    void shouldSaveComment() {
        Comment comment = generator.nextObject(Comment.class);
        CommentDto commentDto = new CommentDto(comment.getText());
        Item item = generator.nextObject(Item.class);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));

        Booking booking = generator.nextObject(Booking.class);
        when(bookingRepository.findFirstByBookerIdAndItemIdAndEndBefore(
                Mockito.anyLong(), Mockito.anyLong(), Mockito.any(LocalDateTime.class)))
                .thenReturn(Optional.ofNullable(booking));

        when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        CommentResponse commentResponse = new CommentResponse(
                comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getCreated());

        CommentResponse savedComment = itemService.saveComment(user.getId(), commentDto, item.getId());
        assertEquals(commentResponse.getText(), savedComment.getText());
    }

    @Test
    void shouldSearchItems() {
        Item item1 = generator.nextObject(Item.class);
        Item item2 = generator.nextObject(Item.class);
        Page<Item> itemsPage = new PageImpl<>(List.of(item1, item2));
        when(itemRepository.searchItems(Mockito.anyString(), Mockito.any())).thenReturn(itemsPage);
        List<Item> searchedItems = itemService.searchItems("GOOD", 0,10);
        assertEquals(2, searchedItems.size());
    }

    @Test
    void shouldNotSearchItemsWithEmptyText() {
        List<Item> searchedItems = itemService.searchItems("", 0,10);
        assertEquals(0, searchedItems.size());
    }

}