package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@NonNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                              @Valid @RequestBody ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        return ResponseEntity.ok().body(itemMapper.toItemDto(itemService.saveItem(userId, item)));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long itemId,
                                              @NonNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestBody ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        return ResponseEntity.ok().body(itemMapper.toItemDto(itemService.updateItem(itemId, userId, item)));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long itemId) {
        Item item;
        try {
            item = itemService.getItemById(itemId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Товар с id " + itemId + " не найден");
        }
        return ResponseEntity.ok().body(itemMapper.toItemDto(item));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItemsUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok().body(itemService.getAllItemsUser(userId)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchAvailableItems(@RequestParam String text) {
        return ResponseEntity.ok().body(itemService.searchItems(text)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList()));
    }
}