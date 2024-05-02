package ru.mirea.infinitejourneysbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.infinitejourneysbackend.domain.dto.country.CountryRequest;
import ru.mirea.infinitejourneysbackend.domain.model.Country;
import ru.mirea.infinitejourneysbackend.exception.country.CountryNotFoundProblem;
import ru.mirea.infinitejourneysbackend.exception.country.CountryNotUniqueNameProblem;
import ru.mirea.infinitejourneysbackend.repository.CountryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository repository;

    public Country save(Country country) {
        return repository.save(country);
    }

    public Country create(Country country) {
        if (existByName(country.getName())) {
            throw new CountryNotUniqueNameProblem(country.getName());
        }
        return save(country);
    }

    public Optional<Country> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Country> findByName(String name) {
        return repository.findByName(name);
    }

    public boolean existByName(String name) {
        return repository.existsByName(name);
    }

    public Country getById(Long id) {
        return findById(id).orElseThrow(() -> new CountryNotFoundProblem(id.toString()));
    }

    public List<Country> getAll() {
        return repository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        Country country = getById(id);
        country.getTours().forEach(tour -> tour.setCountry(null));
        repository.delete(country);
    }

    public Country update(CountryRequest request, Long countryId) {
        var country = getById(countryId);
        String name = request.name();

        if (!country.getName().equals(name) && existByName(name)) {
            throw new CountryNotUniqueNameProblem(name);
        }

        country.setName(name);
        return save(country);
    }
}

