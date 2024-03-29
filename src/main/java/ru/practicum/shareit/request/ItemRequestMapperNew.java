package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;

@UtilityClass
public class ItemRequestMapperNew {

    public static ItemRequest toItemRequest(ItemRequestDto request) {
        return new ItemRequest(
                request.getId(),
                request.getDescription(),
               null,
                LocalDateTime.now());
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest request) {
        Long requestorId;
        if (request.getRequestor() == null) {
            requestorId = null;
        } else {
            requestorId = request.getRequestor().getId();
        }
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                requestorId,
                request.getCreated());
    }

    public static ItemRequest toItemRequest(ItemRequestResponse request) {
        return new ItemRequest(
                request.getId(),
                request.getDescription(),
                null,
                LocalDateTime.now());
    }

    public static ItemRequestResponse toItemRequestResponse(ItemRequest request) {
        Long requestorId = null;
        if (request.getRequestor() != null) {
            requestorId = request.getRequestor().getId();
        }
        return new ItemRequestResponse(
                request.getId(),
                request.getDescription(),
                requestorId,
                request.getCreated(),
                new ArrayList<>());
    }
}
