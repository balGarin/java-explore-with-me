package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.dto.CategoryShortDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto addNewCategory(CategoryShortDto categoryShortDto) {
        Category category = categoryRepository.save(categoryMapper.toCategory(categoryShortDto));
        log.info("Добавлена новая категория - {}", category);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteCategory(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        categoryRepository.delete(category);
        log.info("Категория с Id= {}, была успешно удалена", catId);
    }

    @Override
    public CategoryDto updateCategory(CategoryShortDto categoryShortDto, Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        category.setName(categoryShortDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        log.info("Категория с Id= {}, была изменена на - {}", catId, updatedCategory);
        return categoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        log.info("Найдена категория - {}", category);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        Sort sort = Sort.by("id").ascending();
        PageRequest pageRequest = PageRequest.of(from, size, sort);
        Page<Category> all = categoryRepository.findAll(pageRequest);
        List<Category> categories = all.stream().toList();
        log.info("Найденные категории : {}", categories);
        return categoryMapper.toCategoryDto(categories);
    }
}
