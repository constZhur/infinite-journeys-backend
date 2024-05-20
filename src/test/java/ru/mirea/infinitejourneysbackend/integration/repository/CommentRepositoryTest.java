package ru.mirea.infinitejourneysbackend.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.mirea.infinitejourneysbackend.domain.model.*;
import ru.mirea.infinitejourneysbackend.integration.AbstractIntegrationTest;
import ru.mirea.infinitejourneysbackend.repository.CommentRepository;
import ru.mirea.infinitejourneysbackend.repository.TourRepository;
import ru.mirea.infinitejourneysbackend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSave() {
        Comment comment = Comment.builder()
                .content("Содержание комментария")
                .build();

        Comment savedComment = commentRepository.save(comment);

        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("Содержание комментария");
    }

    @Test
    public void testFindById() {
        Comment comment = Comment.builder()
                .content("Содержание комментария")
                .build();

        Comment savedComment = commentRepository.save(comment);

        Optional<Comment> foundOptionalComment = commentRepository.findById(savedComment.getId());

        assertThat(foundOptionalComment).isPresent();

        Comment foundComment = foundOptionalComment.get();

        assertThat(foundComment).isNotNull();
        assertThat(foundComment.getContent()).isEqualTo(savedComment.getContent());
    }

    @Test
    public void testDelete() {
        Comment comment = Comment.builder()
                .content("Содержание комментария")
                .build();

        Comment savedComment = commentRepository.save(comment);
        commentRepository.delete(comment);

        Optional<Comment> optionalComment = commentRepository.findById(savedComment.getId());
        assertThat(optionalComment).isEmpty();
    }

    @Test
    public void testFindAllByTourId() {
        Tour tour = Tour.builder()
                .title("Тур")
                .description("Информация о туре")
                .build();

        Comment firstComment = Comment.builder()
                .content("Содержание первого комментария")
                .tour(tour)
                .build();

        Comment secondComment = Comment.builder()
                .content("Содержание второго комментария")
                .tour(tour)
                .build();

        Tour savedTour = tourRepository.save(tour);
        commentRepository.save(firstComment);
        commentRepository.save(secondComment);

        Page<Comment> commentsByTourId = commentRepository.findAllByTourId(savedTour.getId(), PageRequest.of(0, 10));

        assertThat(commentsByTourId.getTotalElements()).isEqualTo(2L);
        assertThat(commentsByTourId.getContent()).containsExactly(firstComment, secondComment);
    }

    @Test
    public void testFindAllByParentId() {
        Comment parentComment = Comment.builder()
                .content("Родительский комментарий")
                .build();

        Comment savedParentComment = commentRepository.save(parentComment);

        Comment childComment1 = Comment.builder()
                .content("Первый дочерний комментарий")
                .parent(savedParentComment)
                .build();

        Comment childComment2 = Comment.builder()
                .content("Второй дочерний комментарий")
                .parent(savedParentComment)
                .build();

        commentRepository.save(childComment1);
        commentRepository.save(childComment2);

        Page<Comment> commentsByParentId = commentRepository.findAllByParentId(savedParentComment.getId(), PageRequest.of(0, 10));

        assertThat(commentsByParentId.getTotalElements()).isEqualTo(2L);
        assertThat(commentsByParentId.getContent()).containsExactly(childComment1, childComment2);
    }

    @Test
    public void testDeleteAllByAuthorId() {
        User user = User.builder()
                .username("Тестовый пользователь")
                .email("user@test.com")
                .password("password123")
                .role(Role.ROLE_ADMIN)
                .gender(Gender.MALE)
                .build();

        Comment comment = Comment.builder()
                .content("Содержание комментария")
                .author(user)
                .build();

        User savedUser = userRepository.save(user);
        Comment savedComment = commentRepository.save(comment);

        commentRepository.deleteAllByAuthorId(savedUser.getId());

        List<Comment> allComments = commentRepository.findAll();
        assertThat(allComments).doesNotContain(savedComment);
    }
}
