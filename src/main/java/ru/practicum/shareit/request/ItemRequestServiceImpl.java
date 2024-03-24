package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto2;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
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
    private final ItemRequestMapper mapper;

    @Override
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
        return repository.findByRequestorId(userId).stream().map(x ->
                getItemRequestById(x.getId(), userId)).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestResponse> getAllItemRequests(Long userId, Integer from, Integer size) {
        checkUser(userId);
        if (from < 0 || size < 0) {
            throw new ValidationException("Неверный from или size");
        }
        PageRequest page = PageRequest.of(from, size, Sort.by(DESC, "created"));
        return repository.findAllByRequestorIdNotOrderByCreatedDesc(userId, page));
    }

    @Override
    public ItemRequestResponse getItemRequestById(Long requestId, Long userId) {
        checkUser(userId);
        ItemRequest itemRequest = repository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с id " + requestId + " не найден"));
        return mapper.toItemRequestResponse(itemRequest);
    }

    private void checkUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " не найден"));
    }
}
