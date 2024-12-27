package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryShortDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addNewCategory(CategoryShortDto categoryShortDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(CategoryShortDto categoryShortDto, Long catId);

    CategoryDto getCategoryById(Long catId);

    List<CategoryDto> getAllCategories(Integer from, Integer size);

}
