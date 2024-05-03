package ru.mirea.infinitejourneysbackend.domain.dto.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "Базовое DTO для страницы")
public class PageRequestDTO {
    @Schema(description = "Номер страницы", example = "0")
    @Min(value = 0, message = "Номер страницы не может быть меньше 0")
    private Integer page;

    @Schema(description = "Количество элементов на странице", example = "10")
    @Min(value = 1, message = "Количество элементов на странице не может быть меньше 1")
    @Max(value = 10000, message = "Количество элементов на странице не может быть больше 10000")
    private Integer size;
}

