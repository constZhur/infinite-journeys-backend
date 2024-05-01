package ru.mirea.infinitejourneysbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.infinitejourneysbackend.domain.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}
