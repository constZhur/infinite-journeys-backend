package ru.mirea.infinitejourneysbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.infinitejourneysbackend.domain.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
