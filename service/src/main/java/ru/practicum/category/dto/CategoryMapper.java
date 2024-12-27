package ru.practicum.category.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.category.model.Category;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    Category toCategory(CategoryShortDto categoryDto);

    CategoryDto toCategoryDto(Category category);

    List<CategoryDto>toCategoryDto(List<Category>categories);
}
