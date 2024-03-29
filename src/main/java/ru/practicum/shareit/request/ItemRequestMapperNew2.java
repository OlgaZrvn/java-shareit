package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestDto2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapperNew2 {

    public static ItemRequest toItemRequest(ItemRequestDto2 itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }

    public static ItemRequestDto2 toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto2 itemRequestDto = new ItemRequestDto2(
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setItems(new ArrayList<>());
        return itemRequestDto;
    }

    public static List<ItemRequestDto2> toItemRequestDtos(List<ItemRequest> itemRequests) {
        return itemRequests.stream().map(ItemRequestMapperNew2::toItemRequestDto).collect(Collectors.toList());
    }
}
