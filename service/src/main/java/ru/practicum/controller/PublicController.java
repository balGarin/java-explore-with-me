package ru.practicum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.service.CategoryService;

@RestController
public class PublicController {

    private  final CategoryService categoryService;

    public PublicController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<Object>getAllCategories(@RequestParam(name = "from",defaultValue ="0")Integer from,
                                                  @RequestParam(name = "size",defaultValue = "10")Integer size){
      return ResponseEntity.status(200).body(categoryService.getAllCategories(from,size));
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<Object> getCategoryById(@PathVariable(name = "catId")Long catId){
    return ResponseEntity.status(200).body(categoryService.getCategoryById(catId));

    }
}
