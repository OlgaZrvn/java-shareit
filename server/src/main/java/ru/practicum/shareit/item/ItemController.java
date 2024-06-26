package ru.practicum.shareit.item;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponse> createNewItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestBody ItemDto itemDto) {
        return ResponseEntity.ok().body(itemService.saveItem(userId, itemDto));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long itemId,
                                                   @NonNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestBody ItemDto itemDto) {
        Item item = ItemMapperNew.toItem(itemDto);
        return ResponseEntity.ok().body(itemService.updateItem(itemId, userId, item));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long itemId,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok().body(itemService.getItemById(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                @RequestParam(defaultValue = "0") Integer from,
                                                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(itemService.getAllItemsUser(userId, from, size));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestParam String text,
                                                     @RequestParam(defaultValue = "0") Integer from,
                                                     @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(itemService.searchItems(text, from, size)
                .stream()
                .map(ItemMapperNew::toItemDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentResponse> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestBody CommentDto commentDto,
                                                         @PathVariable("itemId") Long itemId) {
        return ResponseEntity.ok().body(itemService.saveComment(userId, commentDto, itemId));
    }
}