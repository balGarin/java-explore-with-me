package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.StatDtoIn;
import ru.practicum.service.StatService;

@RestController
@Slf4j
public class StatsController {

    private final StatService service;

    @Autowired
    public StatsController(StatService service) {
        this.service = service;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.OK)
    public void addStat(@Valid @RequestBody StatDtoIn statDto) {
        service.addStat(statDto);
        log.info("Добавлена новая запись {}", statDto);
//        return ResponseEntity.status(201).body(
//                "Информация сохранена");
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStat(@RequestParam(name = "start") String start,
                                          @RequestParam(name = "end") String end,
                                          @RequestParam(name = "uris", required = false) String[] uris,
                                          @RequestParam(name = "unique", required = false) boolean unique) {
        log.info("Запрос на получение статистики : start:{} , end:{} , uris:{} , unique:{}", start, end, uris, unique);
        return ResponseEntity.status(200).body(service.getStat(start, end, uris, unique));
    }

}
