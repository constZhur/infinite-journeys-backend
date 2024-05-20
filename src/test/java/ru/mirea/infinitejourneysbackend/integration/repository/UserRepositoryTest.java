package ru.mirea.infinitejourneysbackend.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.mirea.infinitejourneysbackend.domain.model.Gender;
import ru.mirea.infinitejourneysbackend.domain.model.Role;
import ru.mirea.infinitejourneysbackend.domain.model.User;
import ru.mirea.infinitejourneysbackend.integration.AbstractIntegrationTest;
import ru.mirea.infinitejourneysbackend.repository.UserRepository;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSave() {
        User user = User.builder()
                .username("Тестовый пользователь")
                .email("user@test.com")
                .password("password123")
                .role(Role.ROLE_ADMIN)
                .gender(Gender.MALE)
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("Тестовый пользователь");
        assertThat(savedUser.getEmail()).isEqualTo("user@test.com");
        assertThat(savedUser.getPassword()).isEqualTo("password123");
        assertThat(savedUser.getRole()).isEqualTo(Role.ROLE_ADMIN);
        assertThat(savedUser.getGender()).isEqualTo(Gender.MALE);
    }

    @Test
    public void testFindById() {
        User user = User.builder()
                .username("Тестовый пользователь")
                .email("user@test.com")
                .password("password123")
                .role(Role.ROLE_ADMIN)
                .gender(Gender.MALE)
                .build();

        User savedUser = userRepository.save(user);

        Optional<User> foundOptionalUser = userRepository.findById(savedUser.getId());

        assertThat(foundOptionalUser).isPresent();

        User foundUser = foundOptionalUser.get();

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(foundUser.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(foundUser.getPassword()).isEqualTo(savedUser.getPassword());
        assertThat(foundUser.getRole()).isEqualTo(savedUser.getRole());
        assertThat(foundUser.getGender()).isEqualTo(savedUser.getGender());
    }

    @Test
    public void testFindByUsername() {
        User user = User.builder()
                .username("Тестовый пользователь")
                .email("user@test.com")
                .password("password123")
                .role(Role.ROLE_ADMIN)
                .gender(Gender.MALE)
                .build();

        User savedUser = userRepository.save(user);

        Optional<User> foundOptionalUser = userRepository.findByUsername(savedUser.getUsername());

        assertThat(foundOptionalUser).isPresent();

        User foundUser = foundOptionalUser.get();

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(foundUser.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(foundUser.getPassword()).isEqualTo(savedUser.getPassword());
        assertThat(foundUser.getRole()).isEqualTo(savedUser.getRole());
        assertThat(foundUser.getGender()).isEqualTo(savedUser.getGender());
    }

    @Test
    public void testFindAllWithFilter() {
        User firstUser = User.builder()
                .username("user1")
                .email("user1@test.com")
                .password("password123")
                .role(Role.ROLE_BUYER)
                .build();

        User secondUser = User.builder()
                .username("user2")
                .email("user2@test.com")
                .password("password123")
                    .role(Role.ROLE_SELLER)
                .build();

        User thirdUser = User.builder()
                .username("admin")
                .email("admin@test.com")
                .password("password123")
                .role(Role.ROLE_ADMIN)
                .build();

        userRepository.save(firstUser);
        userRepository.save(secondUser);
        userRepository.save(thirdUser);

        Page<User> foundUsers = userRepository.findAllWithFilter(null,
                "user", "test.com", null, false,
                OffsetDateTime.now(), PageRequest.of(0, 10));

        assertThat(foundUsers).isNotNull();
        assertThat(foundUsers.getContent()).hasSize(2);
        assertThat(foundUsers.getContent().get(0).getUsername()).isEqualTo("user1");
        assertThat(foundUsers.getContent().get(1).getUsername()).isEqualTo("user2");
    }

    @Test
    public void testExistsByUsername() {
        User user = User.builder()
                .username("Тестовый пользователь")
                .email("user@test.com")
                .password("password123")
                .role(Role.ROLE_ADMIN)
                .gender(Gender.MALE)
                .build();

        userRepository.save(user);
        boolean isExist = userRepository.existsByUsername("Тестовый пользователь");

        assertThat(isExist).isTrue();
    }

    @Test
    public void testExistsByEmail() {
        User user = User.builder()
                .username("Тестовый пользователь")
                .email("user@test.com")
                .password("password123")
                .role(Role.ROLE_ADMIN)
                .gender(Gender.MALE)
                .build();

        userRepository.save(user);
        boolean isExist = userRepository.existsByEmail("user@test.com");

        assertThat(isExist).isTrue();
    }
}
