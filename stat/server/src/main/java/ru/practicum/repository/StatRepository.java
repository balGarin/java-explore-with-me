package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.StatDtoOut;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Stat, Integer> {


    @Query(value = """
            SELECT new ru.practicum.StatDtoOut(s.app,s.uri,COUNT(s.ip))
            FROM Stat s
            WHERE s.timestamp BETWEEN :start and :end
            GROUP BY s.uri,s.app
            ORDER BY COUNT(s.ip) DESC""")
    List<StatDtoOut> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = """
            SELECT new ru.practicum.StatDtoOut(s.app,s.uri,COUNT(s.ip))
            FROM Stat s
            WHERE (s.timestamp BETWEEN :start and :end)
            AND s.uri IN :uris
            GROUP BY s.uri,s.app
            ORDER BY COUNT(s.ip) DESC""")
    List<StatDtoOut> findAllByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query(value = """
            SELECT new ru.practicum.StatDtoOut(s.app,s.uri,COUNT(DISTINCT s.ip))
            FROM Stat s
            WHERE s.timestamp BETWEEN :start and :end
            GROUP BY s.uri,s.app
            ORDER BY COUNT(s.ip) DESC""")
    List<StatDtoOut> findAllByTimestampBetweenAndDistinctByIp(LocalDateTime start, LocalDateTime end);

    @Query(value = """
            SELECT new ru.practicum.StatDtoOut(s.app,s.uri,COUNT(DISTINCT s.ip))
            FROM Stat s
            WHERE (s.timestamp BETWEEN :start and :end)
            AND s.uri IN :uris
            GROUP BY s.uri,s.app
            ORDER BY COUNT(s.ip) DESC""")
    List<StatDtoOut> findAllByUriInAndTimestampBetweenAndDistinctByIp(LocalDateTime start,
                                                                      LocalDateTime end, String[] uris);

}
