package ru.mirea.infinitejourneysbackend.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.mirea.infinitejourneysbackend.domain.model.UploadedFile;
import ru.mirea.infinitejourneysbackend.integration.AbstractIntegrationTest;
import ru.mirea.infinitejourneysbackend.repository.UploadedFileRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UploadedFileRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    UploadedFileRepository uploadedFileRepository;

    @Test
    public void testSave() {
        UploadedFile uploadedFile = UploadedFile.builder()
                .originalName("cat.jpg")
                .extension("jpg")
                .path("/images/cat.jpg")
                .size(1024L)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        UploadedFile savedFile = uploadedFileRepository.save(uploadedFile);

        assertThat(savedFile).isNotNull();
        assertThat(savedFile.getOriginalName()).isEqualTo("cat.jpg");
        assertThat(savedFile.getExtension()).isEqualTo("jpg");
        assertThat(savedFile.getSize()).isEqualTo(1024L);
    }

    @Test
    public void testFindAll() {
        List<UploadedFile> uploadedFiles = uploadedFileRepository.findAll();
        assertThat(uploadedFiles).isNotNull();
    }

    @Test
    public void testFindById() {
        UploadedFile uploadedFile = UploadedFile.builder()
                .originalName("cat.jpg")
                .extension("jpg")
                .path("/images/cat.jpg")
                .size(1024L)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        UploadedFile savedFile = uploadedFileRepository.save(uploadedFile);

        Optional<UploadedFile> optionalFile = uploadedFileRepository.findById(savedFile.getId());

        assertThat(optionalFile).isPresent();
        assertThat(optionalFile.get().getId()).isEqualTo(savedFile.getId());
    }

    @Test
    public void testDeleteById() {
        UploadedFile uploadedFile = UploadedFile.builder()
                .originalName("cat.jpg")
                .extension("jpg")
                .path("/images/cat.jpg")
                .size(1024L)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        UploadedFile savedFile = uploadedFileRepository.save(uploadedFile);

        uploadedFileRepository.deleteById(savedFile.getId());

        Optional<UploadedFile> optionalFile = uploadedFileRepository.findById(savedFile.getId());
        assertThat(optionalFile).isEmpty();
    }
}
