package ru.practicum.shareit.request;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapperNew;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    private ItemRequestService itemRequestService;

    @Mock
    ItemRequestRepository repository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;

    private final User user = new User(0L,"User", "user@ya.ru");

    private final ItemRequest itemRequest = new ItemRequest(0L,"Request1", user, LocalDateTime.now());

    private final EasyRandom generator = new EasyRandom();

    @BeforeEach
    void setUp() {
        itemRequestService = new ItemRequestServiceImpl(repository, userRepository, itemRepository);
    }

    @Test
    void shouldNotSaveNewItemRequest() {
        assertThrows(NotFoundException.class, () -> itemRequestService.saveItemRequest(user.getId(),
                ItemRequestMapperNew.toItemRequestDto(itemRequest)));
    }

    @Test
    void shouldSaveNewItemRequest() {
        when(repository.save(Mockito.any())).thenReturn(itemRequest);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(userRepository.getReferenceById(Mockito.anyLong())).thenReturn(user);
        ItemRequestDto itemRequestDto = ItemRequestMapperNew.toItemRequestDto(itemRequest);
        ItemRequestDto savedItemRequest = itemRequestService.saveItemRequest(user.getId(), itemRequestDto);
        itemRequestDto.setCreated(savedItemRequest.getCreated());
        assertEquals(itemRequestDto, savedItemRequest);
    }

    @Test
    void shouldNotFindItemRequestByUser() {
        assertThrows(NotFoundException.class, () ->  itemRequestService.getAllUserItemRequests(user.getId()));
    }

    @Test
    void shouldFind2ItemRequestByUser() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        List<ItemRequest> requests = List.of(
                generator.nextObject(ItemRequest.class),
                generator.nextObject(ItemRequest.class)
        );
        List<Item> items = List.of(
                generator.nextObject(Item.class),
                generator.nextObject(Item.class)
        );
        List<ItemRequestResponse> itemRequestResponses = requests
                        .stream()
                        .map(ItemRequestMapperNew::toItemRequestResponse)
                        .collect(Collectors.toList());
        when(repository.findAllByRequestorId(Mockito.any())).thenReturn(requests);
        when(itemRepository.findByRequestIdIn(Mockito.any())).thenReturn(items);
        List<ItemRequestResponse> returnedRequests = itemRequestService.getAllUserItemRequests(user.getId());
        assertEquals(itemRequestResponses.size(), returnedRequests.size());
    }

    @Test
    void shouldNotFindAllItemRequestWithoutUser() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getAllItemRequests(
                user.getId(), 0, 0));
    }

    @Test
    void shouldNotFindAllItemRequest() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        assertThrows(ValidationException.class, () -> itemRequestService.getAllItemRequests(
                user.getId(), -1, -1));
    }

   /* @Test
    void shouldFind2ItemRequest() {
        List<ItemRequest> requests = List.of(
                generator.nextObject(ItemRequest.class),
                generator.nextObject(ItemRequest.class)
        );
        List<Item> items = List.of(
                generator.nextObject(Item.class),
                generator.nextObject(Item.class)
        );
        List<ItemRequestResponse> itemRequestResponses = requests
                .stream()
                .map(ItemRequestMapperNew::toItemRequestResponse)
                .collect(Collectors.toList());

        PageRequest page = PageRequest.of(0, 10, Sort.by(DESC, "created"));
        Page<ItemRequest> itemRequestList =
        when(repository.findAllByRequestorIdNotOrderByCreatedDesc(Mockito.anyLong(), Mockito.any(PageRequest.class)))
                .thenReturn();

        List<ItemRequestResponse> returnedRequests =
                itemRequestService.getAllItemRequests(user.getId(), 0, 10);
        assertEquals(itemRequestResponses.size(), returnedRequests.size());
    }

    */

    @Test
    void shouldNotFindItemRequestByIdWithoutUser() {
        assertThrows(NotFoundException.class, () ->  itemRequestService.getItemRequestById(0L, user.getId()));
    }

    @Test
    void shouldNotFindItemRequestByIdWithoutItemRequest() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        assertThrows(NotFoundException.class, () ->  itemRequestService.getItemRequestById(0L, user.getId()));
    }

    @Test
    void shouldFindItemRequestById() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(itemRequest));
        List<Item> items = List.of(
                generator.nextObject(Item.class),
                generator.nextObject(Item.class)
        );
        when(itemRepository.findByRequestId(Mockito.anyLong()))
                .thenReturn(items);
        ItemRequestResponse savedRequest = ItemRequestMapperNew.toItemRequestResponse(itemRequest);
        savedRequest.setItems(items
                .stream()
                .map(ItemMapperNew::toItemDto)
                .collect(Collectors.toList()));
        ItemRequestResponse returnedRequest = itemRequestService.getItemRequestById(0L, user.getId());
        assertEquals(savedRequest.getId(), returnedRequest.getId());
        assertEquals(savedRequest.getDescription(), returnedRequest.getDescription());
        assertEquals(savedRequest.getCreated(), returnedRequest.getCreated());
        assertEquals(savedRequest.getItems().size(), returnedRequest.getItems().size());
    }

}