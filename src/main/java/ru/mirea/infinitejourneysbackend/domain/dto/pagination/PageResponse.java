package ru.mirea.infinitejourneysbackend.domain.dto.pagination;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private int totalPages;

    private long totalSize;

    private int pageNumber;

    private int pageSize;

    private List<T> content;
}
