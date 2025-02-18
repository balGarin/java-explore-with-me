package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentDtoIn;
import ru.practicum.comment.dto.CommentDtoOut;
import ru.practicum.comment.dto.CommentUpdateDto;

import java.util.List;

public interface CommentService {
    CommentDtoOut addNewComment(Long userId, CommentDtoIn commentDtoIn);

    CommentDtoOut editOwnComment(Long userId, Long commentId, CommentUpdateDto commentDtoIn);

    void deleteOwnComment(Long userId, Long commentId);

    CommentDtoOut getCommentByIdOfCurrentUser(Long userId, Long commentId);

    List<CommentDtoOut> getAllCommentsByCurrentUser(Long userId, Integer from, Integer size);

}
