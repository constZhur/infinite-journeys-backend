package ru.mirea.infinitejourneysbackend.domain.dto.tour;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import ru.mirea.infinitejourneysbackend.validation.constraints.ValidFiles;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record UpdateTourRequest(
        String title,

        String description,

        OffsetDateTime startDate,

        OffsetDateTime endDate,

        Long countryId,

        List<UUID> deletedAttachments,

        @ValidFiles
        List<MultipartFile> attachments
) { }
