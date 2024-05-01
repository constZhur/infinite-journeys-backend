package ru.mirea.infinitejourneysbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.infinitejourneysbackend.domain.model.TourFileRelation;

@Repository
public interface TourFileRelationRepository extends JpaRepository<TourFileRelation, Long> {
}
