package ru.practicum.shareit.item;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.comment.CommentMapperNew;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto2;
import ru.practicum.shareit.item.dto.ItemResponse2;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
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

    private ItemMapper itemMapper;

    private final EasyRandom generator = new EasyRandom();

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository, bookingRepository,
                new ItemMapperImpl(), new CommentMapperNew());
        itemMapper = new ItemMapperImpl();
    }

    @Test
    void testNotSaveItemWithNoOwner() {
        ItemDto2 item = generator.nextObject(ItemDto2.class);
        assertThrows(NotFoundException.class, () ->  itemService.saveItem(0L, item));
    }

    @Test
    void testSaveOneItem() {
        User owner = new User(0L, "user@ya.ru", "User1");
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));
        ItemDto2 itemDto2 = generator.nextObject(ItemDto2.class);
        Item item = itemMapper.toItem(itemDto2);
        item.setOwner(owner);
        when(itemRepository.save(Mockito.any())).thenReturn(item);
        ItemResponse2 itemResponse = itemMapper.toItemResponse2(itemRepository.save(item));
        if (itemDto2.getRequestId() != null) {
            itemResponse.setRequestId(itemDto2.getRequestId());
        }
        ItemResponse2 returnedItem = itemService.saveItem(0L, itemDto2);
        assertEquals(itemResponse, returnedItem);
    }

 /*   @Test
    void testGet2Items() {
        User owner = new User(0L, "user@ya.ru", "User1");
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));

        List<ItemResponse2> items = new ArrayList<>();
        items.add(generator.nextObject(ItemResponse2.class));
        items.add(generator.nextObject(ItemResponse2.class));

        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        when(itemRepository.findByOwnerId(0L, page).stream().map(x ->
               itemService.getItemById(x.getId(), 0L)).collect(toList())).thenReturn(items);
        List<ItemResponse2> savedItems = itemService.getAllItemsUser(0L, 0, 10);
        assertEquals(2, savedItems.size());
    }

  */

    @Test
    void testNotFindItemByIdWithoutItem() {
        assertThrows(NotFoundException.class, () ->  itemService.getItemById(0L, 0L));
    }

    @Test
    void testNotFindItemByIdWithoutUser() {
        Item item = generator.nextObject(Item.class);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        assertThrows(NotFoundException.class, () ->  itemService.getItemById(item.getId(), 0L));
    }

    @Test
    void testNotUpdateItemWithoutUser() {
        Item item = generator.nextObject(Item.class);
        assertThrows(NotFoundException.class, () ->  itemService.updateItem(item.getId(), 0L, item));
    }

    @Test
    void testNotUpdateItemWithUserNotOwner() {
        Item item = generator.nextObject(Item.class);
        User owner = new User(0L, "owner@ya.ru", "Owner1");
        item.setOwner(owner);
        User user = new User(1L, "user@ya.ru", "User");
        when(itemRepository.getReferenceById(Mockito.anyLong())).thenReturn(item);
        when(userRepository.getReferenceById(Mockito.anyLong())).thenReturn(user);
        assertThrows(NotFoundException.class, () ->  itemService.checkOwner(item.getId(), user.getId()));
    }

    @Test
    void testUpdateItem() {
        Item item = generator.nextObject(Item.class);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
        User owner = new User(0L, "owner@ya.ru", "Owner1");
        item.setOwner(owner);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));
        ItemResponse2 updatedItem = itemService.getItemById(item.getId(), owner.getId());
        when(itemRepository.save(Mockito.any())).thenReturn(itemMapper.toItem(updatedItem));
        when(itemRepository.getReferenceById(Mockito.anyLong())).thenReturn(item);
        when(userRepository.getReferenceById(Mockito.anyLong())).thenReturn(owner);
        ItemResponse2 returnedItem = itemService.updateItem(item.getId(), owner.getId(),itemMapper.toItem(updatedItem));
        assertEquals(updatedItem, returnedItem);
    }

    @Test
    void testSearchItems() {
        Page<Item> items;
        generator.nextObject(Item.class);
        when(itemRepository.searchItems(Mockito.anyString(), Mockito.any())).thenReturn();
    }
}