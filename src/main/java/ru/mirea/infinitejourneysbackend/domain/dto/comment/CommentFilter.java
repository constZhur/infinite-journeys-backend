package ru.mirea.infinitejourneysbackend.domain.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.mirea.infinitejourneysbackend.domain.dto.pagination.PageRequestDTO;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "Запрос комментариев по фильтру")
public class CommentFilter extends PageRequestDTO {

    @Schema(description = "Идентификатор тура", example = "1")
    @NotNull(message = "Id тура является обязательным параметром")
    private Long tourId;

}

