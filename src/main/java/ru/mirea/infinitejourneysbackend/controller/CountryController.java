package ru.mirea.infinitejourneysbackend.controller;

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
public class CountryController {
    private final CountryService service;
    private final CountryMapper mapper;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CountryResponse create(@RequestBody @Valid CountryRequest request) {
        Country entity = mapper.toEntity(request);
        Country country = service.create(entity);
        return mapper.toResponse(country);
    }

    @GetMapping("/{countryId}")
    public CountryResponse getById(@PathVariable Long countryId) {
        Country country = service.getById(countryId);
        return mapper.toResponse(country);
    }

    @GetMapping
    public List<CountryResponse> getAll() {
        List<Country> countries = service.getAll();
        return mapper.toResponse(countries);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{countryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long countryId) {
        service.deleteById(countryId);
    }

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
