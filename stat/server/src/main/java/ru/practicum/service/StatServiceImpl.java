package ru.practicum.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.StatDtoIn;
import ru.practicum.StatDtoOut;
import ru.practicum.model.StatMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.stereotype.Service;
import ru.practicum.repository.StatRepository;

@Service
@Slf4j
public class StatServiceImpl implements StatService {

    private final StatRepository repository;

    private final StatMapper statMapper;


    @Autowired
    public StatServiceImpl(StatRepository repository, StatMapper statMapper) {
        this.repository = repository;
        this.statMapper = statMapper;
    }

    @Override
    public void addStat(StatDtoIn statDto) {
        repository.save(statMapper.fromDto(statDto));

    }

    @Override
    public List<StatDtoOut> getStat(String start, String end, String[] uris, Boolean unique) {
        List<StatDtoOut> stats;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);
        if (unique) {
            if (uris != null) {
                stats = repository.findAllByUriInAndTimestampBetweenAndDistinctByIp(startTime, endTime, uris);
                log.info("Статистика с уникальными IP и  с выбранными Uri собрана : {}", start);
            } else {
                stats = repository.findAllByTimestampBetweenAndDistinctByIp(startTime, endTime);
                log.info("Статистика с уникальными IP собрана : {}", start);
            }
        } else {
            if (uris != null) {
                stats = repository.findAllByTimestampBetweenAndUriIn(startTime, endTime, uris);
                log.info("Статистика без уникальных IP и  с выбранными Uri собрана : {}", start);

            } else {
                stats = repository.findAllByTimestampBetween(startTime, endTime);
                log.info("Статистика без уникальных IP собрана : {}", start);
            }
        }
        return stats;
    }
}
