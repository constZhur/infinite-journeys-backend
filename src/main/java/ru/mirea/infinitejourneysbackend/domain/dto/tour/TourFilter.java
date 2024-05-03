package ru.mirea.infinitejourneysbackend.domain.dto.tour;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.mirea.infinitejourneysbackend.domain.dto.pagination.PageRequestDTO;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Запрос туров по фильтру")
public class TourFilter extends PageRequestDTO {

    @Schema(description = "Идентификатор страны", example = "1")
    private Long countryId;
}
