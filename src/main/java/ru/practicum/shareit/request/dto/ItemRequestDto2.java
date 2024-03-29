package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto2;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto2 {
    private Long id;
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    private LocalDateTime created;
    private List<ItemDto2> items;

    public ItemRequestDto2(String description, LocalDateTime created) {
        this.description = description;
        this.created = created;
    }
}
