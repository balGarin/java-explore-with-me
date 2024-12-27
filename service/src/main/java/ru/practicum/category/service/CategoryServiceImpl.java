package ru.practicum.category.service;

import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.dto.CategoryShortDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDto addNewCategory(CategoryShortDto categoryShortDto) {
        Category category = categoryRepository.save(categoryMapper.toCategory(categoryShortDto));
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteCategory(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(()->new NotFoundException("Category with id="+catId+" was not found"));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDto updateCategory(CategoryShortDto categoryShortDto, Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(()->new NotFoundException("Category with id="+catId+" was not found"));
        category.setName(categoryShortDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(()->new NotFoundException("Category with id="+catId+" was not found"));
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        return  categoryMapper.toCategoryDto(categoryRepository.findAllCategories(from, size));
    }
}
