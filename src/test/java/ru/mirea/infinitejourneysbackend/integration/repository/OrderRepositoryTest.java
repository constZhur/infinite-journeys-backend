package ru.mirea.infinitejourneysbackend.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.mirea.infinitejourneysbackend.domain.model.*;
import ru.mirea.infinitejourneysbackend.integration.AbstractIntegrationTest;
import ru.mirea.infinitejourneysbackend.repository.OrderRepository;
import ru.mirea.infinitejourneysbackend.repository.TourRepository;
import ru.mirea.infinitejourneysbackend.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TourRepository tourRepository;

    @Test
    public void testSave() {
        User user = User.builder()
                .username("Тестовый пользователь")
                .email("user@test.com")
                .password("password123")
                .role(Role.ROLE_ADMIN)
                .gender(Gender.MALE)
                .build();
        userRepository.save(user);

        Tour tour = Tour.builder()
                .title("Тур")
                .description("Информация о туре")
                .build();
        tourRepository.save(tour);

        Order order = Order.builder()
                .buyer(user)
                .tour(tour)
                .build();
        Order savedOrder = orderRepository.save(order);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getTour()).isEqualTo(tour);
        assertThat(savedOrder.getBuyer()).isEqualTo(user);
    }
}
