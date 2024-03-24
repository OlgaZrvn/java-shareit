package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestDto> createNewItemRequest(@NonNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                                               @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return ResponseEntity.ok().body(itemRequestService.saveItemRequest(userId, itemRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestResponse>> getAllUserItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok().body((itemRequestService.getAllUserItemRequests(userId)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestResponse>> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                        @RequestParam(defaultValue = "0") Integer from,
                                                                        @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(itemRequestService.getAllItemRequests(userId, from, size));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestResponse> getItemRequestById(@PathVariable Long requestId,
                                                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok().body(itemRequestService.getItemRequestById(requestId, userId));
    }
}
