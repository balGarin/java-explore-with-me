package ru.practicum.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.Client;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.EventGetRequestDto;
import ru.practicum.event.service.EventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class PublicController {

    private final CategoryService categoryService;
    private final EventService eventService;
    private final Client client;


    public PublicController(CategoryService categoryService, EventService eventService, Client client) {
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.client = client;
    }

    @GetMapping("/categories")
    public ResponseEntity<Object> getAllCategories(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.status(200).body(categoryService.getAllCategories(from, size));
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<Object> getCategoryById(@PathVariable(name = "catId") Long catId) {
        return ResponseEntity.status(200).body(categoryService.getCategoryById(catId));

    }

    @GetMapping("/events")
    public ResponseEntity<Object> getEvents(@RequestParam(name = "text",required = false) String text,
                                            @RequestParam(name = "categories") Integer[] categories,
                                            @RequestParam(name = "paid", required = false) Boolean paid,
                                            @RequestParam(name = "rangeStart",required = false)String rangeStart,
                                            @RequestParam(name = "rangeEnd",required = false)String rangeEnd,
                                            @RequestParam(name = "onlyAvailable",defaultValue = "false")Boolean onlyAvailable,
                                            @RequestParam(name = "sort")String sort,
                                            @RequestParam(name = "from",defaultValue = "0")Integer from,
                                            @RequestParam(name = "size",defaultValue = "10")Integer size,
                                            HttpServletRequest request) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EventGetRequestDto eventGetRequestDto = EventGetRequestDto.builder()
                .text(text.toLowerCase()).categories(categories).paid(paid)
                .rangeStart(LocalDateTime.parse(rangeStart,dateTimeFormatter))
                .rangeEnd(LocalDateTime.parse(rangeEnd,dateTimeFormatter))
                .onlyAvailable(onlyAvailable).sort(EventGetRequestDto.Sort.valueOf(sort))
                .from(from).size(size).build();
        return ResponseEntity.status(200).body(eventService.getEvents(
                eventGetRequestDto,request));

    }
}
