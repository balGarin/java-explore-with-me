package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request,Long> {

    Request findByEventIdAndRequesterId(Long eventId, Long requesterId);

    List<Request>findAllByEventId(Long eventId);


    List<Request> findAllByIdInAndEventIdIsOrderByIdAsc(Long[]ids, Long eventId);


    List<Request> findAllByRequesterIdNot(Long requesterId);
}
