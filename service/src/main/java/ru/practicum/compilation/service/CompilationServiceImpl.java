package ru.practicum.compilation.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDtoIn;
import ru.practicum.compilation.dto.CompilationDtoOut;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.QCompilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.IncorrectDataException;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    public CompilationServiceImpl(EventRepository eventRepository,
                                  CompilationRepository compilationRepository,
                                  CompilationMapper compilationMapper) {
        this.eventRepository = eventRepository;
        this.compilationRepository = compilationRepository;
        this.compilationMapper = compilationMapper;
    }

    @Override
    public CompilationDtoOut addNewCompilation(CompilationDtoIn compilationDto) {
        List<Event> events = new ArrayList<>();
        if (compilationDto.getEvents() != null) {
            events = eventRepository.findAllById(compilationDto.getEvents());
        }
        Compilation compilation = compilationMapper.toCompilation(compilationDto, events);
        log.info("Добавлена новая подборка - {}", compilation);
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        compilationRepository.delete(compilation);
        log.info("Подборка с Id= {}, успешно удалена", compId);
    }

    @Override
    public List<CompilationDtoOut> getCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from, size);
        QCompilation compilation = QCompilation.compilation;
        if (pinned != null) {
            BooleanExpression booleanExpression = compilation.pinned.eq(pinned);
            Page<Compilation> all = compilationRepository.findAll(booleanExpression, pageRequest);
            List<Compilation> compilations = all.stream().toList();
            log.info("Получен список подборок : {}", compilations);
            return compilationMapper.toCompilationDto(compilations);
        } else {
            Page<Compilation> all = compilationRepository.findAll(pageRequest);
            List<Compilation> compilations = all.stream().toList();
            log.info("Получен список подборок : {}", compilations);
            return compilationMapper.toCompilationDto(compilations);
        }


    }

    @Override
    public CompilationDtoOut getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        log.info("Получена  подборка : {}", compilation);
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public CompilationDtoOut updateCompilation(Long compId, CompilationDtoIn compilationDtoIn) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));

        if (compilationDtoIn.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(compilationDtoIn.getEvents());
            compilation.setEvents(events);
        }
        if (compilationDtoIn.getPinned() != null) compilation.setPinned(compilationDtoIn.getPinned());
        if (compilationDtoIn.getTitle() != null) {
            if (compilationDtoIn.getTitle().length() > 50 || compilationDtoIn.getTitle().length() < 1) {
                throw new IncorrectDataException("Title =" + compilationDtoIn.getTitle() + " is invalid");
            }
            compilation.setTitle(compilationDtoIn.getTitle());
        }
        log.info("Подборка с Id= {},была изменена на - {}", compId, compilation);
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }
}
