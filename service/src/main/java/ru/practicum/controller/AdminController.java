package ru.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryShortDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.EventUpdateAdminDto;
import ru.practicum.event.service.EventService;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;

    private final EventService eventService;

    public AdminController(UserService userService, CategoryService categoryService, EventService eventService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.eventService = eventService;
    }

    @PostMapping("/users")
    public ResponseEntity<Object> addNewUser(@RequestBody @Valid UserShortDto userShortDto) {
        return ResponseEntity.status(201).body(userService.annNewUser(userShortDto));
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers(@RequestParam(name = "ids", required = false) Long[] ids,
                                     @RequestParam(name = "from", defaultValue = "0") Integer from,
                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return userService.getAllUsers(ids, from, size);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Object> removeUser(@PathVariable(name = "userId") Long userId) {
        userService.removeUser(userId);
        return ResponseEntity.status(204).body("Пользователь удален");
    }

    @PostMapping("/categories")
    public ResponseEntity<Object> addNewCategory(@RequestBody @Valid CategoryShortDto categoryShortDto) {
        return ResponseEntity.status(201).body(categoryService.addNewCategory(categoryShortDto));
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable(name = "catId") Long catId) {
        categoryService.deleteCategory(catId);
        return ResponseEntity.status(204).body("Категория удалена");
    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<Object> updateCategory(@RequestBody @Valid CategoryShortDto categoryShortDto,
                                                 @PathVariable(name = "catId") Long catId) {
        return ResponseEntity.status(200).body(categoryService.updateCategory(categoryShortDto, catId));

    }

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<Object> editEventByAdmin(@RequestBody EventUpdateAdminDto eventUpdateAdminDto,
                                                   @PathVariable(name = "eventId") Long eventId) {
        return ResponseEntity.status(200).body(eventService.editEventByAdmin(eventUpdateAdminDto,eventId));
    }
}
