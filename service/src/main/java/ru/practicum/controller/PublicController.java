package ru.practicum.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.service.CategoryService;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.event.service.EventService;

@RestController
@Slf4j
public class PublicController {

    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;


    public PublicController(CategoryService categoryService,
                            EventService eventService,
                            CompilationService compilationService) {
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.compilationService = compilationService;
    }

    @GetMapping("/categories")
    public ResponseEntity<Object> getAllCategories(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск категорий с RequestParams : {},{}", from, size);
        return ResponseEntity.status(200).body(categoryService.getAllCategories(from, size));
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<Object> getCategoryById(@PathVariable(name = "catId") Long catId) {
        log.info("Получен запрос на поиск категории с Id= {}", catId);
        return ResponseEntity.status(200).body(categoryService.getCategoryById(catId));

    }

    @GetMapping("/events")
    public ResponseEntity<Object> getEvents(@RequestParam(name = "text", required = false) String text,
                                            @RequestParam(name = "categories",required = false) Integer[] categories,
                                            @RequestParam(name = "paid", required = false) Boolean paid,
                                            @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                            @RequestParam(name = "onlyAvailable",
                                                    defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(name = "sort", required = false) String sort,
                                            @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        log.info("Получен запрос на поиск событий с RequestParams : {},{},{},{},{},{},{},{},{}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        return ResponseEntity.status(200).body(eventService.getEvents(text,
                categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request));

    }

    @GetMapping("events/{eventId}")
    public ResponseEntity<Object> getEventById(
            @PathVariable(name = "eventId") Long eventId, HttpServletRequest request) {
        log.info("Получен запрос на поиск события с Id= {}", eventId);
        return ResponseEntity.status(200).body(eventService.getEventById(eventId, request));
    }

    @GetMapping("compilations")
    public ResponseEntity<Object> getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                  @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск подборок с RequestParams : {},{},{}", pinned, from, size);
        return ResponseEntity.status(200).body(compilationService.getCompilations(pinned, from, size));
    }

    @GetMapping("compilations/{compId}")
    public ResponseEntity<Object> getCompilationById(@PathVariable(name = "compId") Long compId) {
        log.info("Получен запрос на поиск подборки с Id= {} ", compId);
        return ResponseEntity.status(200).body(compilationService.getCompilationById(compId));
    }
}
