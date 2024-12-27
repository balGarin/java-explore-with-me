package ru.practicum.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = """
            SELECT *
            FROM users AS u
            WHERE u.user_id IN :ids
            GROUP BY u.user_id
            ORDER BY u.user_id ASC
            LIMIT :size OFFSET :from""",nativeQuery = true)
    List<User> findAllInIds(Long[] ids, Integer from, Integer size);

    @Query(value = """
            SELECT *
            FROM users AS u
            GROUP BY u.user_id
            ORDER BY u.user_id ASC
            LIMIT :size OFFSET :from""",nativeQuery = true)
    List<User> findAllWithoutIds(Integer from, Integer size);
}
