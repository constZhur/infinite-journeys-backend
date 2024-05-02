package ru.mirea.infinitejourneysbackend.domain.dto.tour;

import org.springframework.web.multipart.MultipartFile;
import ru.mirea.infinitejourneysbackend.validation.constraints.ValidFiles;

import java.util.List;

public record TourRequest(
        String title,

        String description,

        Long countryId,

        @ValidFiles
        List<MultipartFile> attachments
) { }
