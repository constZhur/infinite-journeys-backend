package ru.mirea.infinitejourneysbackend.unit.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import ru.mirea.infinitejourneysbackend.domain.dto.file.UploadedFileContentResponse;
import ru.mirea.infinitejourneysbackend.domain.model.UploadedFile;
import ru.mirea.infinitejourneysbackend.exception.file.FileDeleteProblem;
import ru.mirea.infinitejourneysbackend.exception.file.FileDownloadProblem;
import ru.mirea.infinitejourneysbackend.exception.file.FileNotFoundProblem;
import ru.mirea.infinitejourneysbackend.exception.file.FileSaveProblem;
import ru.mirea.infinitejourneysbackend.repository.UploadedFileRepository;
import ru.mirea.infinitejourneysbackend.service.FileService;
import ru.mirea.infinitejourneysbackend.config.CloudStorageConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Mock
    private AmazonS3 yandexS3;

    @Mock
    private UploadedFileRepository repository;

    @Mock
    private CloudStorageConfig cloudStorageConfig;

    @InjectMocks
    private FileService fileService;

    @Test
    void uploadFilesAndGetIds_Success() throws Exception {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.getBytes()).thenReturn("test content".getBytes());
        when(multipartFile.getSize()).thenReturn(12L);

        UploadedFile uploadedFile = UploadedFile.builder()
                .id(UUID.randomUUID())
                .originalName("test")
                .extension("txt")
                .path(UUID.randomUUID().toString())
                .size(12L)
                .build();

        when(repository.save(any(UploadedFile.class))).thenReturn(uploadedFile);
        when(cloudStorageConfig.getBucketName()).thenReturn("test-bucket");

        List<UUID> ids = fileService.uploadFilesAndGetIds(new MultipartFile[]{multipartFile});

        assertThat(ids).hasSize(1);
        verify(yandexS3).putObject(eq("test-bucket"), anyString(), any(ByteArrayInputStream.class), any(ObjectMetadata.class));
        verify(repository).save(any(UploadedFile.class));
    }

    @Test
    void uploadFiles_Success() throws Exception {
        MultipartFile multipartFile1 = mock(MultipartFile.class);
        MultipartFile multipartFile2 = mock(MultipartFile.class);
        when(multipartFile1.getOriginalFilename()).thenReturn("test1.txt");
        when(multipartFile2.getOriginalFilename()).thenReturn("test2.txt");
        when(multipartFile1.getBytes()).thenReturn("test content 1".getBytes());
        when(multipartFile2.getBytes()).thenReturn("test content 2".getBytes());
        when(multipartFile1.getSize()).thenReturn(15L);
        when(multipartFile2.getSize()).thenReturn(15L);

        UploadedFile uploadedFile1 = UploadedFile.builder()
                .id(UUID.randomUUID())
                .originalName("test1")
                .extension("txt")
                .path(UUID.randomUUID().toString())
                .size(15L)
                .build();

        UploadedFile uploadedFile2 = UploadedFile.builder()
                .id(UUID.randomUUID())
                .originalName("test2")
                .extension("txt")
                .path(UUID.randomUUID().toString())
                .size(15L)
                .build();

        when(repository.save(any(UploadedFile.class))).thenReturn(uploadedFile1).thenReturn(uploadedFile2);
        when(cloudStorageConfig.getBucketName()).thenReturn("test-bucket");

        List<UploadedFile> uploadedFiles = fileService.uploadFiles(List.of(multipartFile1, multipartFile2));

        assertThat(uploadedFiles).hasSize(2);
        verify(yandexS3, times(2)).putObject(eq("test-bucket"), anyString(), any(ByteArrayInputStream.class), any(ObjectMetadata.class));
        verify(repository, times(2)).save(any(UploadedFile.class));
    }

    @Test
    void getAll_Success() {
        UploadedFile uploadedFile1 = UploadedFile.builder()
                .id(UUID.randomUUID())
                .originalName("test1")
                .extension("txt")
                .path(UUID.randomUUID().toString())
                .size(15L)
                .build();

        UploadedFile uploadedFile2 = UploadedFile.builder()
                .id(UUID.randomUUID())
                .originalName("test2")
                .extension("txt")
                .path(UUID.randomUUID().toString())
                .size(15L)
                .build();

        when(repository.findAll()).thenReturn(List.of(uploadedFile1, uploadedFile2));

        List<UploadedFile> uploadedFiles = fileService.getAll();

        assertThat(uploadedFiles).hasSize(2);
        assertThat(uploadedFiles).containsExactly(uploadedFile1, uploadedFile2);
        verify(repository).findAll();
    }

    @Test
    void saveFile_FileSaveProblem() throws Exception {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn(null);

        assertThatThrownBy(() -> fileService.uploadFilesAndGetIds(new MultipartFile[]{multipartFile}))
                .isInstanceOf(FileSaveProblem.class);
    }

    @Test
    void downloadFile_Success() throws Exception {
        UUID fileId = UUID.randomUUID();
        String filePath = UUID.randomUUID().toString();

        UploadedFile uploadedFile = UploadedFile.builder()
                .id(fileId)
                .originalName("test")
                .extension("txt")
                .path(filePath)
                .size(12L)
                .build();

        S3Object s3Object = mock(S3Object.class);
        S3ObjectInputStream s3ObjectInputStream = new S3ObjectInputStream(new ByteArrayInputStream("test content".getBytes()), null);
        when(s3Object.getObjectContent()).thenReturn(s3ObjectInputStream);

        when(repository.findById(fileId)).thenReturn(Optional.of(uploadedFile));
        when(yandexS3.getObject(anyString(), eq(filePath))).thenReturn(s3Object);
        when(cloudStorageConfig.getBucketName()).thenReturn("test-bucket");

        UploadedFileContentResponse response = fileService.downloadFile(fileId);

        assertThat(response.name()).isEqualTo("test.txt");
        assertThat(response.content()).isEqualTo("test content".getBytes());
        verify(yandexS3).getObject("test-bucket", filePath);
    }

    @Test
    void downloadFile_ThrowsFileDownloadProblem() throws Exception {
        UUID fileId = UUID.randomUUID();
        String filePath = UUID.randomUUID().toString();

        UploadedFile uploadedFile = UploadedFile.builder()
                .id(fileId)
                .originalName("test")
                .extension("txt")
                .path(filePath)
                .size(12L)
                .build();

        when(repository.findById(fileId)).thenReturn(Optional.of(uploadedFile));
        when(cloudStorageConfig.getBucketName()).thenReturn("test-bucket");
        doThrow(new RuntimeException("S3 error")).when(yandexS3).getObject(anyString(), eq(filePath));

        assertThatThrownBy(() -> fileService.downloadFile(fileId))
                .isInstanceOf(FileDownloadProblem.class);
    }


    @Test
    void downloadFile_FileNotFound() {
        UUID fileId = UUID.randomUUID();
        when(repository.findById(fileId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fileService.downloadFile(fileId))
                .isInstanceOf(FileNotFoundProblem.class);
    }

    @Test
    void saveInCloud_ThrowsFileSaveProblem() throws Exception {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getBytes()).thenReturn("test content".getBytes());
        when(multipartFile.getSize()).thenReturn(12L);
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(cloudStorageConfig.getBucketName()).thenReturn("test-bucket");

        doThrow(new RuntimeException("S3 error")).when(yandexS3).putObject(anyString(), anyString(), any(ByteArrayInputStream.class), any(ObjectMetadata.class));

        assertThatThrownBy(() -> fileService.saveInCloud(multipartFile, "test-code"))
                .isInstanceOf(FileSaveProblem.class);
    }

    @Test
    void deleteById_Success() {
        UUID fileId = UUID.randomUUID();
        UploadedFile uploadedFile = UploadedFile.builder()
                .id(fileId)
                .originalName("test")
                .extension("txt")
                .path(UUID.randomUUID().toString())
                .size(12L)
                .build();

        when(repository.findById(fileId)).thenReturn(Optional.of(uploadedFile));
        when(cloudStorageConfig.getBucketName()).thenReturn("test-bucket");

        fileService.deleteById(fileId);

        verify(yandexS3).deleteObject(anyString(), anyString());
        verify(repository).deleteById(fileId);
    }

    @Test
    void deleteById_FileNotFound() {
        UUID fileId = UUID.randomUUID();
        when(repository.findById(fileId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fileService.deleteById(fileId))
                .isInstanceOf(FileNotFoundProblem.class);
    }

    @Test
    void deleteById_FileDeleteProblem() {
        UUID fileId = UUID.randomUUID();
        UploadedFile uploadedFile = UploadedFile.builder()
                .id(fileId)
                .originalName("test")
                .extension("txt")
                .path(UUID.randomUUID().toString())
                .size(12L)
                .build();

        when(repository.findById(fileId)).thenReturn(Optional.of(uploadedFile));
        when(cloudStorageConfig.getBucketName()).thenReturn("test-bucket");
        doThrow(new RuntimeException("Deletion error")).when(yandexS3).deleteObject(anyString(), anyString());

        assertThatThrownBy(() -> fileService.deleteById(fileId))
                .isInstanceOf(FileDeleteProblem.class);
    }
}

