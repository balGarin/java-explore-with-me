package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long>, QuerydslPredicateExecutor<Event> {
    @Query(value = """
            SELECT *
            FROM events ev
            WHERE ev.user_id =:userId
            GROUP BY ev.event_id
            ORDER BY ev.event_id ASC
            LIMIT :size OFFSET :from
            """,nativeQuery = true)
    List<Event>findAllEventsByCurrentUser(Long userId,Integer from,Integer size);

    Event findByInitiatorIdAndId(Long userId,Long eventId);
}
