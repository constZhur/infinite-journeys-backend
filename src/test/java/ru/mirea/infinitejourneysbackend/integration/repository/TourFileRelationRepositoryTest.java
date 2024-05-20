package ru.mirea.infinitejourneysbackend.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.mirea.infinitejourneysbackend.domain.model.Tour;
import ru.mirea.infinitejourneysbackend.domain.model.TourFileRelation;
import ru.mirea.infinitejourneysbackend.domain.model.UploadedFile;
import ru.mirea.infinitejourneysbackend.integration.AbstractIntegrationTest;
import ru.mirea.infinitejourneysbackend.repository.TourFileRelationRepository;
import ru.mirea.infinitejourneysbackend.repository.TourRepository;
import ru.mirea.infinitejourneysbackend.repository.UploadedFileRepository;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TourFileRelationRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private TourFileRelationRepository tourFileRelationRepository;

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    @Test
    public void testDelete(){
        Tour tour = Tour.builder()
                .title("Тур")
                .description("Информация о туре")
                .build();
        tourRepository.save(tour);

        UploadedFile uploadedFile = UploadedFile.builder()
                .originalName("cat.jpg")
                .extension("jpg")
                .path("/images/cat.jpg")
                .size(1024L)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        uploadedFileRepository.save(uploadedFile);

        TourFileRelation tourFileRelation = TourFileRelation.builder()
                .tour(tour)
                .file(uploadedFile)
                .build();
        TourFileRelation savedTourFileRelation = tourFileRelationRepository.save(tourFileRelation);

        tourFileRelationRepository.delete(tourFileRelation);

        assertThat(tourFileRelationRepository.findById(savedTourFileRelation.getId())).isEmpty();
    }
}
