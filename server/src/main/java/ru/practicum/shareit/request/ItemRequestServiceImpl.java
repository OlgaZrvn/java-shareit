package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapperNew;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto saveItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        checkUser(userId);
        ItemRequest itemRequest = ItemRequestMapperNew.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(userRepository.getReferenceById(userId));
        repository.save(itemRequest);
        log.info("Создан новый запрос");
        return ItemRequestMapperNew.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestResponse> getAllUserItemRequests(Long userId) {
        checkUser(userId);
        List<ItemRequest> itemRequestList = repository.findAllByRequestorId(userId);
        List<ItemRequestResponse> itemRequestResponseList = addItems(itemRequestList);
        log.info("Получен список запросов пользователя с id {}", userId);
        return itemRequestResponseList;
    }

    @Override
    public List<ItemRequestResponse> getAllItemRequests(Long userId, Integer from, Integer size) {
        checkUser(userId);
        if (from < 0 || size < 0) {
            throw new ValidationException("Неверный from или size");
        }
        PageRequest page = PageRequest.of(from / size, size, Sort.by(DESC, "created"));
        Page<ItemRequest> itemRequestList = repository.findAllByRequestorIdNotOrderByCreatedDesc(userId, page);
        List<ItemRequest> itemRequests = itemRequestList.getContent();
        List<ItemRequestResponse> itemRequestResponseList = addItems(itemRequests);
        log.info("Получен список всех запросов");
        return itemRequestResponseList;
    }

    @Override
    public ItemRequestResponse getItemRequestById(Long requestId, Long userId) {
        checkUser(userId);
        ItemRequest itemRequest = repository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с id " + requestId + " не найден"));

        ItemRequestResponse itemRequestResponse = ItemRequestMapperNew.toItemRequestResponse(itemRequest);

        itemRequestResponse.setItems(itemRepository.findByRequestId(itemRequest.getId())
                .stream()
                .map(ItemMapperNew::toItemDto)
                .collect(Collectors.toList()));

        log.info("Получен запрос с id {}", requestId);
        return itemRequestResponse;
    }

    private void checkUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " не найден"));
    }

    @Transactional
    private List<ItemRequestResponse> addItems(List<ItemRequest> itemRequests) {

        List<ItemRequestResponse> itemRequestResponseList = itemRequests
                .stream()
                .map(ItemRequestMapperNew::toItemRequestResponse)
                .collect(Collectors.toList());
        log.info("Получен список из {} запросов", itemRequestResponseList.size());

        List<Long> itemRequestIds = itemRequests
                .stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());
        log.info("Получен список из {}  id запросов", itemRequestIds.size());

        List<Item> items = itemRepository.findByRequestIdIn(itemRequestIds);
        log.info("Получен список из {} товаров", items.size());

        if (!items.isEmpty()) {
            List<ItemDto> itemDtoList = items
                    .stream()
                    .map(ItemMapperNew::toItemDto)
                    .collect(Collectors.toList());

            Map<Long, List<ItemDto>> itemRequestIdToItems = itemDtoList
                    .stream()
                    .collect(Collectors.groupingBy(ItemDto::getRequestId, Collectors.toList()));

            itemRequestResponseList.forEach(itemRequestResponse ->
                    itemRequestResponse.setItems(itemRequestIdToItems.get(itemRequestResponse.getId())));
        } else {
            itemRequestResponseList.forEach(itemRequestResponse ->
                    itemRequestResponse.setItems(new ArrayList<>()));
        }
        return itemRequestResponseList;
    }
}
