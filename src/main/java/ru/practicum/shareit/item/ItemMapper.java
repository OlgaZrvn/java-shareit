package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "available", target = "available")
    ItemDto toItemDto(Item item);

    @Mapping(source = "name", target = "name")
 //   @Mapping(source = "available", target = "available")
    Item toItem(ItemDto itemDto);
}
