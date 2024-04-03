package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto saveItemRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestResponse> getAllUserItemRequests(Long userId);

    List<ItemRequestResponse> getAllItemRequests(Long userId, Integer from, Integer size);

    ItemRequestResponse getItemRequestById(Long requestId, Long userId);
}
