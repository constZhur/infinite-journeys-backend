package ru.mirea.infinitejourneysbackend.controller;

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
public class FileController {
    private final FileService service;
    private final UploadedFileMapper mapper;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public List<UUID> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        return service.uploadFilesAndGetIds(files);
    }

    @GetMapping
    public List<UploadedFileResponse> getAllFiles() {
        List<UploadedFile> files = service.getAll();
        return mapper.toResponse(files);
    }

    @GetMapping("/{fileId}")
    public UploadedFileResponse getFileById(@PathVariable UUID fileId) {
        UploadedFile file = service.getById(fileId);
        return mapper.toResponse(file);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable UUID fileId) {
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
