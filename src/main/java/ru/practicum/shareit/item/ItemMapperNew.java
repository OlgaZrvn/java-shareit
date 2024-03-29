package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto2;
import ru.practicum.shareit.item.dto.ItemResponse2;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

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

    public static ItemResponse2 toItemResponse(Item item) {
        return new ItemResponse2(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toItem(ItemResponse2 ItemResponse) {
        return new Item(
                ItemResponse.getId(),
                ItemResponse.getName(),
                ItemResponse.getDescription(),
                ItemResponse.getAvailable()
        );
    }

    public static List<ItemDto2> toItemDtos(List<Item> items) {
        return items
                .stream()
                .map(ItemMapperNew::toItemDto).collect(Collectors.toList());
    }

    public static List<ItemResponse2> toItemResponses(List<Item> items) {
        return items.stream().map(ItemMapperNew::toItemResponse).collect(Collectors.toList());
    }
}
