package ru.mirea.infinitejourneysbackend.domain.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.mirea.infinitejourneysbackend.domain.dto.pagination.PageRequestDTO;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "Запрос комментариев из треда по фильтру")
public class CommentThreadFilter extends PageRequestDTO {

    @Schema(description = "Идентификатор комментария", example = "1")
    @NotNull(message = "Идентификатор комментария является обязательным параметром")
    private Long commentId;

}

