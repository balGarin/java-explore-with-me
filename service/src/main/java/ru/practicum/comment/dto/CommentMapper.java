package ru.practicum.comment.dto;

import org.mapstruct.*;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class, EventMapper.class})
public interface CommentMapper {
    @Mapping(target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "commentator", qualifiedByName = "getUser", source = "comment")
    CommentDtoOut toCommentDto(Comment comment);


    List<CommentDtoOut>toCommentDto(List<Comment>comments);



    @Named("getUser")
    default User getCommentator(Comment comment) {
        if (comment.getAnonymous()) {
            return new User(0L, "unknown", "unknown");
        } else {
            return comment.getCommentator();
        }
    }

}
