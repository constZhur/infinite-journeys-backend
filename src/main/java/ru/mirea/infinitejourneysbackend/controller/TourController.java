package ru.mirea.infinitejourneysbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mirea.infinitejourneysbackend.domain.dto.pagination.PageResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.tour.TourFilter;
import ru.mirea.infinitejourneysbackend.domain.dto.tour.TourRequest;
import ru.mirea.infinitejourneysbackend.domain.dto.tour.TourResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.tour.UpdateTourRequest;
import ru.mirea.infinitejourneysbackend.domain.model.Tour;
import ru.mirea.infinitejourneysbackend.mapper.TourMapper;
import ru.mirea.infinitejourneysbackend.service.TourService;

@RestController
@RequestMapping("/tours")
@RequiredArgsConstructor
public class TourController {
    private final TourService service;
    private final TourMapper mapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @ResponseStatus(HttpStatus.CREATED)
    public TourResponse createTour(@RequestBody @Valid TourRequest request) {
        Tour tour = service.create(request);
        return mapper.toResponse(tour);
    }

    @GetMapping("/{tourId}")
    public TourResponse getTourById(@PathVariable Long tourId) {
        Tour tour = service.getById(tourId);
        return mapper.toResponse(tour);
    }

    @DeleteMapping("/{tourId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTour(@PathVariable Long tourId) {
        service.deleteById(tourId);
    }

    @PutMapping("/{tourId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public TourResponse updateTour(
            @RequestBody @Valid UpdateTourRequest request,
            @PathVariable Long tourId
    ) {
        Tour tour = service.update(request, tourId);
        return mapper.toResponse(tour);
    }

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

}
