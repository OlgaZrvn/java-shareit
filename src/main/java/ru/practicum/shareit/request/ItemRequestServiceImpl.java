package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemService itemService;
    private final ItemRequestMapper mapper;

    @Override
    @Transactional
    public ItemRequestDto saveItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        checkUser(userId);
        ItemRequest itemRequest = mapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestorId(userId);
        itemRequest.setCreated(LocalDateTime.now());
        repository.save(itemRequest);
        log.info("Создан новый запрос");
        return mapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestResponse> getAllUserItemRequests(Long userId) {
        checkUser(userId);
        List<ItemRequest> itemRequestList = repository.findAllByRequestorId(userId);
        log.info("Получен список запросов пользователя с id {}", userId);
        return itemRequestList
                .stream()
                .map(mapper::toItemRequestResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestResponse> getAllItemRequests(Long userId, Integer from, Integer size) {
        checkUser(userId);
        if (from < 0 || size < 0) {
            throw new ValidationException("Неверный from или size");
        }
        PageRequest page = PageRequest.of(from, size, Sort.by(DESC, "created"));
        List<ItemRequest> itemRequestList = repository.findAllByRequestorIdNotOrderByCreatedDesc(userId, page);
        log.info("Получен список всех запросов");
        return itemRequestList
                .stream()
                .map(mapper::toItemRequestResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestResponse getItemRequestById(Long requestId, Long userId) {
        checkUser(userId);
        ItemRequest itemRequest = repository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с id " + requestId + " не найден"));
        ItemRequestResponse itemRequestResponse = mapper.toItemRequestResponse(itemRequest);
        itemRequestResponse.setItems(itemService.getItemsByRequestId(requestId));
        log.info("Получен запрос с id {}", requestId);
        return itemRequestResponse;

    }

    private void checkUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " не найден"));
    }
}
