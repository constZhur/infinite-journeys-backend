package ru.mirea.infinitejourneysbackend.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mirea.infinitejourneysbackend.domain.dto.country.CountryRequest;
import ru.mirea.infinitejourneysbackend.domain.model.Country;
import ru.mirea.infinitejourneysbackend.exception.country.CountryNotFoundProblem;
import ru.mirea.infinitejourneysbackend.exception.country.CountryNotUniqueNameProblem;
import ru.mirea.infinitejourneysbackend.repository.CountryRepository;
import ru.mirea.infinitejourneysbackend.service.CountryService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTest {

    @Mock
    private CountryRepository repository;

    @InjectMocks
    private CountryService countryService;

    @Test
    void testSaveCountry() {
        Country country = new Country();
        country.setName("Test Country");

        when(repository.save(any(Country.class))).thenReturn(country);

        Country savedCountry = countryService.save(country);

        verify(repository).save(country);
        assertThat(savedCountry.getName()).isEqualTo("Test Country");
        assertThat(savedCountry).isEqualTo(country);
    }

    @Test
    void testCreateCountryWhenNameIsUnique() {
        Country country = new Country();
        country.setName("Unique Country");

        when(repository.existsByName("Unique Country")).thenReturn(false);
        when(repository.save(any(Country.class))).thenReturn(country);

        Country createdCountry = countryService.create(country);

        verify(repository).existsByName("Unique Country");
        verify(repository).save(country);
        assertThat(createdCountry.getName()).isEqualTo("Unique Country");
        assertThat(createdCountry).isEqualTo(country);
    }

    @Test
    void testCreateCountryWhenNameIsNotUnique() {
        Country country = new Country();
        country.setName("Not Unique Country");

        when(repository.existsByName("Not Unique Country")).thenReturn(true);

        assertThrows(CountryNotUniqueNameProblem.class, () -> countryService.create(country));
        verify(repository).existsByName("Not Unique Country");
        verify(repository, never()).save(any(Country.class));
    }

    @Test
    void testFindById() {
        Country country = new Country();
        country.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(country));

        Optional<Country> foundCountry = countryService.findById(1L);

        assertThat(foundCountry).isPresent();
        assertThat(foundCountry.get().getId()).isEqualTo(1L);
    }

    @Test
    void testFindByName() {
        Country country = new Country();
        country.setName("Test Country");
        when(repository.findByName("Test Country")).thenReturn(Optional.of(country));

        Optional<Country> foundCountry = countryService.findByName("Test Country");

        assertThat(foundCountry).isPresent();
        assertThat(foundCountry.get().getName()).isEqualTo("Test Country");
    }

    @Test
    void testExistByName() {
        when(repository.existsByName("Test Country")).thenReturn(true);

        boolean exists = countryService.existByName("Test Country");

        assertThat(exists).isTrue();
    }

    @Test
    void testGetById() {
        Country country = new Country();
        country.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(country));

        Country foundCountry = countryService.getById(1L);

        assertThat(foundCountry.getId()).isEqualTo(1L);
    }

    @Test
    void testGetByIdWhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CountryNotFoundProblem.class, () -> countryService.getById(1L));
    }

    @Test
    void testGetAll() {
        Country country1 = new Country();
        Country country2 = new Country();
        when(repository.findAll()).thenReturn(List.of(country1, country2));

        List<Country> countries = countryService.getAll();

        assertThat(countries).hasSize(2);
        assertThat(countries).containsExactlyInAnyOrder(country1, country2);
    }

    @Test
    void testDeleteById() {
        Country country = mock(Country.class);
        when(repository.findById(1L)).thenReturn(Optional.of(country));
        when(country.getTours()).thenReturn(Collections.emptyList());

        countryService.deleteById(1L);

        verify(repository).delete(country);
    }

    @Test
    void testUpdateCountryWhenNameIsUnique() {
        Country country = new Country();
        country.setId(1L);
        country.setName("Old Name");

        CountryRequest request = mock(CountryRequest.class);
        when(request.name()).thenReturn("New Name");

        when(repository.findById(1L)).thenReturn(Optional.of(country));
        when(repository.existsByName("New Name")).thenReturn(false);
        when(repository.save(any(Country.class))).thenReturn(country);

        Country updatedCountry = countryService.update(request, 1L);

        verify(repository).findById(1L);
        verify(repository).existsByName("New Name");
        verify(repository).save(country);

        assertThat(country.getName()).isEqualTo("New Name");
        assertThat(updatedCountry).isEqualTo(country);
    }

    @Test
    void testUpdateCountryWhenNameIsNotUnique() {
        Country country = new Country();
        country.setId(1L);
        country.setName("Old Name");

        CountryRequest request = mock(CountryRequest.class);
        when(request.name()).thenReturn("Existing Name");

        when(repository.findById(1L)).thenReturn(Optional.of(country));
        when(repository.existsByName("Existing Name")).thenReturn(true);

        assertThrows(CountryNotUniqueNameProblem.class, () -> countryService.update(request, 1L));
        verify(repository, never()).save(any(Country.class));
    }
}
