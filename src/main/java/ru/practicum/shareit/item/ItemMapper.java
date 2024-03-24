package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDto2;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.ItemResponse2;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item toItem(ItemDto itemDto);

    ItemDto toItemDto(Item item);

    Item toItem(ItemDto2 itemDto2);

    ItemDto2 toItemDto2(Item item);

    Item toItem(ItemResponse itemResponse);

    ItemResponse toItemResponse(Item item);

    Item toItem(ItemResponse2 itemResponse2);

    ItemResponse2 toItemResponse2(Item item);

}
