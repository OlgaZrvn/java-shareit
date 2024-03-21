package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentResponse;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemResponse {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemBooking lastBooking;
    private ItemBooking nextBooking;
    private ItemRequest request;
    private List<CommentResponse> comments;
}
