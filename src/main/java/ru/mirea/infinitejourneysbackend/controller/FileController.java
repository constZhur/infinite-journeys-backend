package ru.mirea.infinitejourneysbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mirea.infinitejourneysbackend.domain.dto.file.UploadedFileResponse;
import ru.mirea.infinitejourneysbackend.domain.model.UploadedFile;
import ru.mirea.infinitejourneysbackend.mapper.UploadedFileMapper;
import ru.mirea.infinitejourneysbackend.service.FileService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "Контроллер файлов", description = "Управление файлами")
@SecurityRequirement(name = "infinite-journeys-api")
public class FileController {
    private final FileService service;
    private final UploadedFileMapper mapper;

    @Operation(summary = "Загрузка файлов",
            description = "Позволяет загружать файлы на сервер.")
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public List<UUID> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        return service.uploadFilesAndGetIds(files);
    }

    @Operation(summary = "Получение всех файлов",
            description = "Позволяет получить список всех загруженных файлов.")
    @GetMapping
    public List<UploadedFileResponse> getAllFiles() {
        List<UploadedFile> files = service.getAll();
        return mapper.toResponse(files);
    }

    @Operation(summary = "Получение файла по ID",
            description = "Позволяет получить информацию о файле по его идентификатору.")
    @GetMapping("/{fileId}")
    public UploadedFileResponse getFileById(@PathVariable("fileId") UUID fileId) {
        UploadedFile file = service.getById(fileId);
        return mapper.toResponse(file);
    }

    @Operation(summary = "Скачивание файла",
            description = "Позволяет скачать файл по его идентификатору.")
    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("fileId") UUID fileId) {
        var file = service.downloadFile(fileId);
        HttpHeaders headers = new HttpHeaders();

        // Добавляем заголовки с информацией о файле
        headers.setContentDispositionFormData("attachment", file.name());
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(file.content().length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(file.content());
    }
}

