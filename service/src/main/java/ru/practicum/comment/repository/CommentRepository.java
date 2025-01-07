package ru.practicum.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long>, QuerydslPredicateExecutor<Comment> {
}