package ru.mirea.infinitejourneysbackend.domain.dto.pagination;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PageRequestDTO {
    @Min(value = 0, message = "Номер страницы не может быть меньше 0")
    private Integer page;

    @Min(value = 1, message = "Количество элементов на странице не может быть меньше 1")
    @Max(value = 10000, message = "Количество элементов на странице не может быть больше 10000")
    private Integer size;
}

