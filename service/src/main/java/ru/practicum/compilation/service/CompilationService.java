package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDtoIn;
import ru.practicum.compilation.dto.CompilationDtoOut;

import java.util.List;

public interface CompilationService {
    CompilationDtoOut addNewCompilation(CompilationDtoIn compilationDto);

    void deleteCompilation(Long compId);

    List<CompilationDtoOut> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDtoOut getCompilationById(Long compId);

    CompilationDtoOut updateCompilation(Long compId, CompilationDtoIn compilationDtoIn);
}

