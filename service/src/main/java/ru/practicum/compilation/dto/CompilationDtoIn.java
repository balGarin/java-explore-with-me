package ru.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CompilationDtoIn {
    private List<Long> events;
    private Boolean pinned = false;
    @NotNull
    @Size(min = 1, max = 50)
    @NotBlank
    private String title;
}
