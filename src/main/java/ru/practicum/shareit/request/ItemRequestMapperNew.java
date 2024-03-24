package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto2;
import ru.practicum.shareit.request.dto.ItemRequestResponse;

import java.util.List;

@Component
@AllArgsConstructor
public class ItemRequestMapperNew {

    private final ItemService itemService;

    public ItemRequestResponse toItemRequestResponse(ItemRequest itemRequest) {
        ItemRequestResponse itemRequestResponse = new ItemRequestResponse();
        itemRequestResponse.setId(itemRequest.getId());
        itemRequestResponse.setDescription(itemRequest.getDescription());
        itemRequestResponse.setRequestorId(itemRequest.getRequestorId());
        itemRequestResponse.setCreated(itemRequest.getCreated());
        List<ItemDto2> items = itemService.getItemsByRequestId(itemRequest.getId());
        itemRequestResponse.setItems(items);
        return itemRequestResponse;
    }
}
