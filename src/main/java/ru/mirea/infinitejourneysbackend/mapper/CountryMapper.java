package ru.mirea.infinitejourneysbackend.mapper;

import org.mapstruct.Mapper;
import ru.mirea.infinitejourneysbackend.domain.dto.country.CountryRequest;
import ru.mirea.infinitejourneysbackend.domain.dto.country.CountryResponse;
import ru.mirea.infinitejourneysbackend.domain.model.Country;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    Country toEntity(CountryRequest request);

    CountryResponse toResponse(Country country);

    List<CountryResponse> toResponse(List<Country> countries);
}
