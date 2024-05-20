package ru.mirea.infinitejourneysbackend.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.mirea.infinitejourneysbackend.domain.model.Country;
import ru.mirea.infinitejourneysbackend.integration.AbstractIntegrationTest;
import ru.mirea.infinitejourneysbackend.repository.CountryRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CountryRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private CountryRepository countryRepository;


    @Test
    public void testSave() {
        Country country = Country.builder()
                .name("Россия")
                .build();

        Country savedCountry = countryRepository.save(country);

        assertThat(savedCountry).isNotNull();
        assertThat(savedCountry.getName()).isEqualTo("Россия");
    }

    @Test
    public void testFindById() {
        Country country = Country.builder()
                .name("Россия")
                .build();

        Country savedCountry = countryRepository.save(country);

        Optional<Country> foundOptionalCountry = countryRepository.findById(savedCountry.getId());

        assertThat(foundOptionalCountry).isPresent();

        Country foundCountry = foundOptionalCountry.get();

        assertThat(foundCountry).isNotNull();
        assertThat(foundCountry.getName()).isEqualTo(savedCountry.getName());

    }

    @Test
    public void testFindByName() {
        Country country = Country.builder()
                .name("Россия")
                .build();

        Country savedCountry = countryRepository.save(country);

        Optional<Country> foundCountryOptional = countryRepository.findByName("Россия");

        assertThat(foundCountryOptional).isPresent();
        assertThat(foundCountryOptional.get().getId()).isEqualTo(savedCountry.getId());
        assertThat(foundCountryOptional.get().getName()).isEqualTo("Россия");
    }

    @Test
    public void testExistsByName() {
        Country country = Country.builder()
                .name("Россия")
                .build();

        countryRepository.save(country);
        boolean isExist = countryRepository.existsByName("Россия");

        assertThat(isExist).isTrue();
    }

    @Test
    public void testFindAll() {
        Country firstCountry = Country.builder()
                .name("Россия")
                .build();

        Country secondCountry = Country.builder()
                .name("США")
                .build();

        countryRepository.save(firstCountry);
        countryRepository.save(secondCountry);

        List<Country> allCountries = countryRepository.findAll();

        assertThat(allCountries).isNotNull();
        assertThat(allCountries.size()).isEqualTo(2);
        assertThat(allCountries).contains(firstCountry);
        assertThat(allCountries).contains(secondCountry);
    }

    @Test
    public void testDelete() {
        Country country = Country.builder()
                .name("Россия")
                .build();

        Country savedCountry = countryRepository.save(country);
        countryRepository.delete(country);

        Optional<Country> optionalCountry = countryRepository.findById(savedCountry.getId());
        assertThat(optionalCountry).isEmpty();
    }
}
