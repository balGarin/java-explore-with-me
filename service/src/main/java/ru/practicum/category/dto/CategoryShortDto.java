package ru.practicum.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryShortDto {
    @NotBlank
    @NotNull
    @Size(max = 50)
    private String name;
}
