package ru.mirea.infinitejourneysbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mirea.infinitejourneysbackend.domain.model.Tour;

import java.util.List;
import java.util.UUID;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    @Query("SELECT t FROM Tour t WHERE " +
            "(:countryId IS null or t.country.id = :countryId) " +
            "AND (:tourName IS null or t.title LIKE %:tourName%)")
    Page<Tour> findAllByCountryIdAndTourName(
            @Param("countryId") Long countryId,
            @Param("tourName") String tourName,
            Pageable pageable);

    List<Tour> findAllBySellerId(UUID sellerId);

}
