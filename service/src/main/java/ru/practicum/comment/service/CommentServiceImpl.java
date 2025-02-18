package ru.practicum.comment.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentDtoIn;
import ru.practicum.comment.dto.CommentDtoOut;
import ru.practicum.comment.dto.CommentMapper;
import ru.practicum.comment.dto.CommentUpdateDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.QComment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDtoOut addNewComment(Long userId, CommentDtoIn commentDtoIn) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " wos not found"));
        Event event = eventRepository.findById(commentDtoIn.getEventId())
                .orElseThrow(() -> new NotFoundException("Event with id=" + commentDtoIn.getEventId() + " wos not found"));
        if (event.getBlockComments()) {
            throw new ConflictException("Event with id=" + event.getId() + " has block for comments");
        }
        Comment comment = new Comment();
        comment.setText(commentDtoIn.getText());
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setAnonymous(commentDtoIn.getAnonymous());
        log.info("Комментарий сохранен - {}", comment);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDtoOut editOwnComment(Long userId, Long commentId, CommentUpdateDto commentUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " wos not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " wos not found"));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("You are not a owner of this comment");
        }
        if (commentUpdateDto.getAnonymous() != null) comment.setAnonymous(commentUpdateDto.getAnonymous());
        if (commentUpdateDto.getText() != null) comment.setText(commentUpdateDto.getText());
        log.info("Комментарий изменен на - {}", comment);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteOwnComment(Long userId, Long commentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " wos not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " wos not found"));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("You are not a owner of this comment");
        }
        commentRepository.delete(comment);
        log.info("Коммент с Id= {},успешно удален", commentId);
    }

    @Override
    public CommentDtoOut getCommentByIdOfCurrentUser(Long userId, Long commentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " wos not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " wos not found"));
        if (!user.getId().equals(comment.getAuthor().getId())) {
            throw new ConflictException("You are not a owner of this comment");
        }
        log.info("Получен коммент - {}", comment);
        return commentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDtoOut> getAllCommentsByCurrentUser(Long userId, Integer from, Integer size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " wos not found"));
        Sort sort = Sort.by("created").descending();
        PageRequest pageRequest = PageRequest.of(from, size, sort);
        QComment qComment = QComment.comment;
        BooleanExpression condition = qComment.author.id.eq(userId);
        Page<Comment> all = commentRepository.findAll(condition, pageRequest);
        List<Comment> comments = all.stream().toList();
        log.info("Найдены комментарии : {}", comments);
        return commentMapper.toCommentDto(comments);
    }
}
