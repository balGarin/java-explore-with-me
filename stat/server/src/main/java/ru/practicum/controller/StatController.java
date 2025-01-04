package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.StatDtoIn;
import ru.practicum.service.StatService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatController {
    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addStat(@Valid @RequestBody StatDtoIn statDto) {
        service.addStat(statDto);
        log.info("Добавлена новая запись {}", statDto);
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
