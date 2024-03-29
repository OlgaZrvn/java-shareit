package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto2;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;

@UtilityClass
public class ItemMapperNew {
    public static ItemDto2 toItemDto(Item item) {
        ItemDto2 itemDto = new ItemDto2(
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
        itemDto.setId(item.getId());
        return itemDto;
    }

    public static Item toItem(ItemDto2 itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );
    }

    public static ItemResponse toItemResponse(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toItem(ItemResponse itemResponse) {
        return new Item(
                itemResponse.getId(),
                itemResponse.getName(),
                itemResponse.getDescription(),
                itemResponse.getAvailable()
        );
    }
}
