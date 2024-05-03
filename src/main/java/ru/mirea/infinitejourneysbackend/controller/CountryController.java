package ru.mirea.infinitejourneysbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mirea.infinitejourneysbackend.domain.dto.country.CountryRequest;
import ru.mirea.infinitejourneysbackend.domain.dto.country.CountryResponse;
import ru.mirea.infinitejourneysbackend.domain.model.Country;
import ru.mirea.infinitejourneysbackend.mapper.CountryMapper;
import ru.mirea.infinitejourneysbackend.service.CountryService;

import java.util.List;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
@Tag(name = "Контроллер стран", description = "Эндпоинты для управления странами")
@SecurityRequirement(name = "infinite-journeys-api")
public class CountryController {
    private final CountryService service;
    private final CountryMapper mapper;

    @Operation(summary = "Создание новой страны",
            description = "Позволяет администраторам создавать новую страну.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CountryResponse create(@RequestBody @Valid CountryRequest request) {
        Country entity = mapper.toEntity(request);
        Country country = service.create(entity);
        return mapper.toResponse(country);
    }

    @Operation(summary = "Получение страны по ID",
            description = "Позволяет получить сведения о стране по ее идентификатору.")
    @GetMapping("/{countryId}")
    public CountryResponse getById(@PathVariable Long countryId) {
        Country country = service.getById(countryId);
        return mapper.toResponse(country);
    }

    @Operation(summary = "Получение всех стран",
            description = "Позволяет получить сведения о всех странах.")
    @GetMapping
    public List<CountryResponse> getAll() {
        List<Country> countries = service.getAll();
        return mapper.toResponse(countries);
    }

    @Operation(summary = "Удаление страны по ID",
            description = "Позволяет администраторам удалить страну по ее идентификатору.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{countryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long countryId) {
        service.deleteById(countryId);
    }

    @Operation(summary = "Обновление страны по ID",
            description = "Позволяет администраторам обновить информацию о стране по ее идентификатору.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{countryId}")
    public CountryResponse update(
            @RequestBody @Valid CountryRequest request,
            @PathVariable Long countryId
    ) {
        Country country = service.update(request, countryId);
        return mapper.toResponse(country);
    }
}

