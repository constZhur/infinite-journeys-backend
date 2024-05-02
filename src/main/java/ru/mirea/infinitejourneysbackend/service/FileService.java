package ru.mirea.infinitejourneysbackend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.mirea.infinitejourneysbackend.config.CloudStorageConfig;
import ru.mirea.infinitejourneysbackend.domain.dto.file.UploadedFileContentResponse;
import ru.mirea.infinitejourneysbackend.domain.model.UploadedFile;
import ru.mirea.infinitejourneysbackend.exception.file.FileDeleteProblem;
import ru.mirea.infinitejourneysbackend.exception.file.FileDownloadProblem;
import ru.mirea.infinitejourneysbackend.exception.file.FileNotFoundProblem;
import ru.mirea.infinitejourneysbackend.exception.file.FileSaveProblem;
import ru.mirea.infinitejourneysbackend.repository.UploadedFileRepository;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class FileService {
    private final AmazonS3 yandexS3;
    private final UploadedFileRepository repository;
    private final CloudStorageConfig cloudStorageConfig;

    private UploadedFile save(UploadedFile file) {
        return repository.save(file);
    }

    public List<UUID> uploadFilesAndGetIds(MultipartFile[] files) {
        return Stream.of(files).map(this::saveFile).map(UploadedFile::getId).toList();
    }

    public List<UploadedFile> uploadFiles(List<MultipartFile> files) {
        return files.stream().map(this::saveFile).toList();
    }

    private UploadedFile saveFile(MultipartFile file) {
        if (isNull(file) || isNull(file.getOriginalFilename())) {
            throw new FileSaveProblem(file.getOriginalFilename());
        }
        String fileCode = UUID.randomUUID().toString();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileNameWithoutExtension = StringUtils.stripFilenameExtension(fileName);
        String extension = StringUtils.getFilenameExtension(fileName);

        saveInCloud(file, fileCode);

        return save(
                UploadedFile.builder()
                        .originalName(fileNameWithoutExtension)
                        .extension(extension)
                        .path(fileCode)
                        .size(file.getSize())
                        .build()
        );
    }

    public void saveInCloud(MultipartFile file, String code) throws FileSaveProblem {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getBytes())) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());

            yandexS3.putObject(cloudStorageConfig.getBucketName(), code, inputStream, metadata);
        } catch (Exception e) {
            throw new FileSaveProblem(file.getOriginalFilename());
        }
    }

    public List<UploadedFile> getAll() {
        return repository.findAll();
    }

    public Optional<UploadedFile> findById(UUID id) {
        return repository.findById(id);
    }

    public UploadedFile getById(UUID id) {
        return findById(id).orElseThrow(
                () -> new FileNotFoundProblem(id.toString())
        );
    }

    public UploadedFileContentResponse downloadFile(UUID fileId) {
        var file = getById(fileId);

        try {
            S3Object s3Object = yandexS3.getObject(cloudStorageConfig.getBucketName(), file.getPath());
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
            byte[] fileContent = IOUtils.toByteArray(objectInputStream);

            return UploadedFileContentResponse.builder()
                    .name(file.getOriginalName() + "." + file.getExtension())
                    .content(fileContent)
                    .build();
        } catch (Exception e) {
            throw new FileDownloadProblem(fileId.toString());
        }
    }

    public void deleteById(UUID fileId) {
        var uploadedFile = getById(fileId);
        try {
            yandexS3.deleteObject(cloudStorageConfig.getBucketName(), uploadedFile.getPath());
        } catch (Exception e) {
            throw new FileDeleteProblem(fileId.toString());
        }
        repository.deleteById(fileId);
    }
}
