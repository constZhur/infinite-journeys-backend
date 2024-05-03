package ru.mirea.infinitejourneysbackend.domain.dto.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    @Schema(description = "Общее количество страниц", example = "10")
    private int totalPages;

    @Schema(description = "Общее количество элементов", example = "100")
    private long totalSize;

    @Schema(description = "Номер страницы", example = "0")
    private int pageNumber;

    @Schema(description = "Количество элементов на странице", example = "10")
    private int pageSize;

    @Schema(description = "Список элементов")
    private List<T> content;
}
