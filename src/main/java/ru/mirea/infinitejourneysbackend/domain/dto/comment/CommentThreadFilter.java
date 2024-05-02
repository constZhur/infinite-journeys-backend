package ru.mirea.infinitejourneysbackend.domain.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.mirea.infinitejourneysbackend.domain.dto.pagination.PageRequestDTO;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentThreadFilter extends PageRequestDTO {

    @NotNull(message = "Id комментария является обязательным параметром")
    private Long commentId;

}

