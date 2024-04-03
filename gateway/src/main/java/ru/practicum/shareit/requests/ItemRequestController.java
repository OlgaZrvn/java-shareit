package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import javax.validation.Valid;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createNewItemRequest(@NonNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return ResponseEntity.ok().body(itemRequestClient.createItemRequest(userId, itemRequestDto));
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestParam(defaultValue = "0") Integer from,
                                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body((itemRequestClient.getAllUserItemRequests(userId, from, size)));
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(defaultValue = "0") Integer from,
                                                     @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(itemRequestClient.getAllItemRequests(userId, from, size));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable Long requestId,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok().body(itemRequestClient.getItemRequest(requestId, userId));
    }
}
