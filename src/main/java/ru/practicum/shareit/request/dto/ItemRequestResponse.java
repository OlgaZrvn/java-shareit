package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestResponse {
    private Long id;
    private String description;
    private Long requestorId;
    private LocalDateTime created;
    private List<ItemDto2> items = new ArrayList<>();

    public ItemRequestResponse(Long id, String description, Long requestorId, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestorId = requestorId;
        this.created = created;
    }
}
