package ru.mirea.infinitejourneysbackend.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.mirea.infinitejourneysbackend.domain.dto.tour.TourResponse;
import ru.mirea.infinitejourneysbackend.domain.model.Tour;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UploadedFileMapper.class, CommentMapper.class})
public interface TourMapper {

    @Named("toResponse")
    TourResponse toResponse(Tour tour);

    @IterableMapping(qualifiedByName = "toResponse")
    List<TourResponse> toResponse(List<Tour> tours);
}
