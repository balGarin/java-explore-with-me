package ru.practicum.service;

import ru.practicum.StatDtoIn;
import ru.practicum.StatDtoOut;

import java.util.List;

public interface StatService {
    void addStat(StatDtoIn statDto);

    List<StatDtoOut> getStat(String start, String end, String[] uris, Boolean unique);
}

