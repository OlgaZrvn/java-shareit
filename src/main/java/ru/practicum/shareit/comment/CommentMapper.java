package ru.practicum.shareit.comment;

import org.mapstruct.Mapper;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentResponse;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentDto commentDto);

    CommentDto toCommentDto(Comment comment);

    Comment toComment(CommentResponse commentResponse);

    CommentResponse toCommentResponse(Comment comment);
}
