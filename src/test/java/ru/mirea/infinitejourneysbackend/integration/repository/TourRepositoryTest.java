package ru.mirea.infinitejourneysbackend.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.mirea.infinitejourneysbackend.domain.model.*;
import ru.mirea.infinitejourneysbackend.integration.AbstractIntegrationTest;
import ru.mirea.infinitejourneysbackend.repository.CountryRepository;
import ru.mirea.infinitejourneysbackend.repository.TourRepository;
import ru.mirea.infinitejourneysbackend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TourRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void testSave() {
        Tour tour = Tour.builder()
                .title("Тур")
                .description("Информация о туре")
                .build();

        Tour savedTour = tourRepository.save(tour);

        assertThat(savedTour).isNotNull();
        assertThat(savedTour.getTitle()).isEqualTo("Тур");
        assertThat(savedTour.getDescription()).isEqualTo("Информация о туре");
    }

    @Test
    public void testFindAll() {
        Tour firstTour = Tour.builder()
                .title("Первый тур")
                .description("Информация о первом туре")
                .build();

        Tour secondTour = Tour.builder()
                .title("Второй тур")
                .description("Информация о втором туре")
                .build();

        tourRepository.save(firstTour);
        tourRepository.save(secondTour);

        List<Tour> allTours = tourRepository.findAll();

        assertThat(allTours).isNotNull();
        assertThat(allTours.size()).isEqualTo(2);
        assertThat(allTours).contains(firstTour);
        assertThat(allTours).contains(secondTour);
    }

    @Test
    public void testFindById() {
        Tour tour = Tour.builder()
                .title("Тур")
                .description("Информация о туре")
                .build();

        Tour savedTour = tourRepository.save(tour);

        Optional<Tour> foundOptionalTour = tourRepository.findById(savedTour.getId());

        assertThat(foundOptionalTour).isPresent();

        Tour foundTour = foundOptionalTour.get();

        assertThat(foundTour).isNotNull();
        assertThat(foundTour.getTitle()).isEqualTo(savedTour.getTitle());
        assertThat(foundTour.getDescription()).isEqualTo(savedTour.getDescription());
    }

    @Test
    public void testFindAllByCountryIdAndTourName() {
        Country country = Country.builder()
                .name("Россия")
                .build();

        Country savedCountry = countryRepository.save(country);

        Tour tour = Tour.builder()
                .title("Тур")
                .description("Информация о туре")
                .country(savedCountry)
                .build();

        tourRepository.save(tour);

        Page<Tour> foundTours = tourRepository.findAllByCountryIdAndTourName(
                savedCountry.getId(), "Тур", PageRequest.of(0, 10));

        assertThat(foundTours).isNotNull();
        assertThat(foundTours).isNotEmpty();
        assertThat(foundTours.getTotalElements()).isEqualTo(1);
        assertThat(foundTours.getContent().get(0).getDescription()).isEqualTo("Информация о туре");
    }


    @Test
    public void testFindAllBySellerId() {
        User user = User.builder()
                .username("Тестовый пользователь")
                .email("user@test.com")
                .password("password123")
                .role(Role.ROLE_ADMIN)
                .gender(Gender.MALE)
                .build();

        Tour firstTour = Tour.builder()
                .title("Первый тур")
                .description("Информация о первом туре")
                .seller(user)
                .build();

        Tour secondTour = Tour.builder()
                .title("Второй тур")
                .description("Информация о втором туре")
                .seller(user)
                .build();

        User savedUser = userRepository.save(user);
        tourRepository.save(firstTour);
        tourRepository.save(secondTour);

        List<Tour> toursBySellerId = tourRepository.findAllBySellerId(savedUser.getId());

        assertThat(toursBySellerId).isNotNull();
        assertThat(toursBySellerId.size()).isEqualTo(2);
        assertThat(toursBySellerId).contains(firstTour);
        assertThat(toursBySellerId).contains(secondTour);
    }

    @Test
    public void testDeleteById() {
        Tour tour = Tour.builder()
                .title("Тур")
                .description("Информация о туре")
                .build();

        Tour savedTour = tourRepository.save(tour);
        tourRepository.deleteById(savedTour.getId());

        Optional<Tour> optionalTour = tourRepository.findById(savedTour.getId());
        assertThat(optionalTour).isEmpty();
    }
}
