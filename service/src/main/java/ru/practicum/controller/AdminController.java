package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryShortDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.compilation.dto.CompilationDtoIn;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.event.dto.EventUpdateAdminDto;
import ru.practicum.event.service.EventService;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;

    private final EventService eventService;

    private final CompilationService compilationService;

    public AdminController(UserService userService, CategoryService categoryService,
                           EventService eventService,
                           CompilationService compilationService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.compilationService = compilationService;
    }

    @PostMapping("/users")
    public ResponseEntity<Object> addNewUser(@RequestBody @Valid UserShortDto userShortDto) {
        log.info("Получен запрос на добавление нового пользователя с RequestBody - {}",userShortDto);
        return ResponseEntity.status(201).body(userService.annNewUser(userShortDto));
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers(@RequestParam(name = "ids", required = false) Long[] ids,
                                     @RequestParam(name = "from", defaultValue = "0") Integer from,
                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен запрос на получение списка пользователей с RequestParams : {},{},{}",ids,from,size);
        return userService.getAllUsers(ids, from, size);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Object> removeUser(@PathVariable(name = "userId") Long userId) {
        log.info("Получен запрос на удаление пользователя с Id = {}",userId);
        userService.removeUser(userId);
        return ResponseEntity.status(204).body("Пользователь удален");
    }

    @PostMapping("/categories")
    public ResponseEntity<Object> addNewCategory(@RequestBody @Valid CategoryShortDto categoryShortDto) {
        log.info("Получен запрос на добавление новой категории с RequestBody - {}",categoryShortDto);
        return ResponseEntity.status(201).body(categoryService.addNewCategory(categoryShortDto));
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable(name = "catId") Long catId) {
        log.info("Получен запрос на удаление категории с Id = {}",catId);
        categoryService.deleteCategory(catId);
        return ResponseEntity.status(204).body("Категория удалена");
    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<Object> updateCategory(@RequestBody @Valid CategoryShortDto categoryShortDto,
                                                 @PathVariable(name = "catId") Long catId) {
        log.info("Получен запрос на обновление категории с Id = {}, и RequestBody - {}",catId,categoryShortDto);
        return ResponseEntity.status(200).body(categoryService.updateCategory(categoryShortDto, catId));

    }

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<Object> editEventByAdmin(@RequestBody @Valid EventUpdateAdminDto eventUpdateAdminDto,
                                                   @PathVariable(name = "eventId") Long eventId) {
        log.info("Получен запрос на обновление события с Id = {}, и RequestBody - {}",eventId,eventUpdateAdminDto);
        return ResponseEntity.status(200).body(eventService.editEventByAdmin(eventUpdateAdminDto, eventId));
    }

    @GetMapping("/events")
    public ResponseEntity<Object> getEventsByAdmin(@RequestParam(name = "users", required = false) Long[] users,
                                                   @RequestParam(name = "states", required = false) String[] states,
                                                   @RequestParam(name = "categories", required = false) Long[] categories,
                                                   @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                   @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                   @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        log.info("Получен запрос на поиск событий с RequestParams : {},{},{},{},{},{},{}",
                users,states,categories,rangeStart,rangeEnd,from,size);
        return ResponseEntity.status(200)
                .body(eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size));

    }

    @PostMapping("/compilations")
    public ResponseEntity<Object> addNewCompilation(@RequestBody @Valid CompilationDtoIn compilationDto) {
        log.info("Получен запрос на добавление новой подборки с RequestBody - {}",compilationDto);
        return ResponseEntity.status(201).body(compilationService.addNewCompilation(compilationDto));
    }

    @DeleteMapping("/compilations/{compId}")
    public ResponseEntity<Object> deleteCompilation(@PathVariable(name = "compId") Long compId) {
        log.info("Получен запрос на удаление подборки с Id = {}",compId);
        compilationService.deleteCompilation(compId);
        return ResponseEntity.status(204).body("Подборка удалена");
    }

    @PatchMapping("compilations/{compId}")
    public ResponseEntity<Object> updateCompilation(@PathVariable(name = "compId") Long compId,
                                                    @RequestBody CompilationDtoIn compilationDtoIn) {
        log.info("Получен запрос на обновление подборки с Id = {}, и RequestBody - {}",compId,compilationDtoIn);
        return ResponseEntity.status(200).body(compilationService.updateCompilation(compId, compilationDtoIn));
    }


}
