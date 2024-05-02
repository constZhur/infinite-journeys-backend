package ru.mirea.infinitejourneysbackend.mapper;
import org.mapstruct.Mapper;
import ru.mirea.infinitejourneysbackend.domain.dto.file.UploadedFileResponse;
import ru.mirea.infinitejourneysbackend.domain.model.TourFileRelation;
import ru.mirea.infinitejourneysbackend.domain.model.UploadedFile;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UploadedFileMapper {
    UploadedFileResponse toResponse(UploadedFile uploadedFileResponse);

    List<UploadedFileResponse> toResponse(List<UploadedFile> uploadedFileResponse);

    default List<UploadedFile> toEntity(List<TourFileRelation> tourFileRelations) {
        if (tourFileRelations == null) {
            return new ArrayList<>();
        }
        return tourFileRelations.stream().map(TourFileRelation::getFile).toList();
    }

    default UploadedFile toEntity(TourFileRelation tourFileRelation) {
        if (tourFileRelation == null) {
            return null;
        }
        return tourFileRelation.getFile();
    }

}
