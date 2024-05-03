package ru.mirea.infinitejourneysbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mirea.infinitejourneysbackend.domain.dto.pagination.PageResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.tour.*;
import ru.mirea.infinitejourneysbackend.domain.model.Tour;
import ru.mirea.infinitejourneysbackend.mapper.TourMapper;
import ru.mirea.infinitejourneysbackend.service.TourService;

@RestController
@RequestMapping("/tours")
@RequiredArgsConstructor
@Tag(name = "Контроллер туров", description = "Управление турами")
@SecurityRequirement(name = "infinite-journeys-api")
public class TourController {
    private final TourService service;
    private final TourMapper mapper;

    @Operation(summary = "Создание тура",
            description = "Позволяет создать новый тур.")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @ResponseStatus(HttpStatus.CREATED)
    public TourResponse createTour(@RequestBody @Valid TourRequest request) {
        Tour tour = service.create(request);
        return mapper.toResponse(tour);
    }

    @Operation(summary = "Получение тура по ID",
            description = "Позволяет получить информацию о туре по его идентификатору.")
    @GetMapping("/{tourId}")
    public TourResponse getTourById(@PathVariable Long tourId) {
        Tour tour = service.getById(tourId);
        return mapper.toResponse(tour);
    }

    @Operation(summary = "Удаление тура",
            description = "Позволяет удалить тур по его идентификатору.")
    @DeleteMapping("/{tourId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTour(@PathVariable Long tourId) {
        service.deleteById(tourId);
    }

    @Operation(summary = "Обновление тура",
            description = "Позволяет обновить информацию о туре.")
    @PutMapping("/{tourId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    public TourResponse updateTour(
            @RequestBody @Valid UpdateTourRequest request,
            @PathVariable Long tourId
    ) {
        Tour tour = service.update(request, tourId);
        return mapper.toResponse(tour);
    }

    @Operation(summary = "Поиск туров по фильтру",
            description = "Позволяет найти туры по заданным критериям.")
    @PostMapping("/filter")
    public PageResponse<TourResponse> findToursByFilter(@RequestBody @Valid TourFilter filter) {
        var result = new PageResponse<TourResponse>();

        var tours = service.findByFilter(filter);
        result.setTotalPages(tours.getTotalPages());
        result.setTotalSize(tours.getTotalElements());
        result.setPageNumber(tours.getNumber());
        result.setPageSize(tours.getSize());
        result.setContent(mapper.toResponse(tours.getContent()));
        return result;
    }

    @Operation(summary = "Обновление цены тура",
            description = "Позволяет обновить цену тура.")
    @PutMapping("/{tourId}/price")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public TourResponse updateTourPrice(
            @RequestBody UpdateTourPriceRequest request,
            @PathVariable Long tourId
    ) {
        Tour tour = service.updateTourPrice(request, tourId);
        return mapper.toResponse(tour);
    }
}

