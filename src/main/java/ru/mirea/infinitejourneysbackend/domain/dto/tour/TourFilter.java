package ru.mirea.infinitejourneysbackend.domain.dto.tour;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.mirea.infinitejourneysbackend.domain.dto.pagination.PageRequestDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class TourFilter extends PageRequestDTO {

    private Long countryId;
}
