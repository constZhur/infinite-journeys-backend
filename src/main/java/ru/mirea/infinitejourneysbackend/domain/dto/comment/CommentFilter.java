package ru.mirea.infinitejourneysbackend.domain.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.mirea.infinitejourneysbackend.domain.dto.pagination.PageRequestDTO;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentFilter extends PageRequestDTO {

    @NotNull(message = "Id тура является обязательным параметром")
    private Long tourId;

}

