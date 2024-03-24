package ru.practicum.shareit.comment;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.dto.CommentResponse;

@Component
public class CommentMapperNew {

    public CommentResponse toCommentResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setText(comment.getText());
        commentResponse.setAuthorName(comment.getAuthor().getName());
        commentResponse.setCreated(comment.getCreated());
        return commentResponse;
    }
}
