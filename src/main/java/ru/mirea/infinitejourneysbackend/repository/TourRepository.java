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

    @Query("select t from Tour t where " +
            "(:countryId is null or t.country.id = :countryId)")
    Page<Tour> findAllByCountryId(
            @Param("countryId") Long countryId,
            Pageable pageable);

    List<Tour> findAllBySellerId(UUID seller_id);

}
