package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponse;
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
    public ResponseEntity<ItemResponse> createNewItem(@NonNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @Valid @RequestBody ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        return ResponseEntity.ok().body(itemService.saveItem(userId, item));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long itemId,
                                                   @NonNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestBody ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        return ResponseEntity.ok().body(itemService.updateItem(itemId, userId, item));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long itemId,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok().body(itemService.getItemById(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItemsUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok().body(itemService.getAllItemsUser(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestParam String text) {
        return ResponseEntity.ok().body(itemService.searchItems(text)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentResponse> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestBody CommentDto commentDto,
                                                         @PathVariable("itemId") Long itemId) {
        return ResponseEntity.ok().body(itemService.saveComment(userId, commentDto, itemId));
    }
}