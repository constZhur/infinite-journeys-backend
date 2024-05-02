package ru.mirea.infinitejourneysbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.mirea.infinitejourneysbackend.domain.model.Comment;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.tour.id = ?1 and c.parent is null")
    Page<Comment> findAllByTourId(Long tourId, Pageable pageable);

    @Query("select c from Comment c where (c.parent != null and c.parent.id = :commentId)")
    Page<Comment> findAllByParentId(
            @Param("commentId") Long commentId,
            Pageable pageable);

    void deleteAllByAuthorId(UUID author_id);
}
