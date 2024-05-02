package ru.mirea.infinitejourneysbackend.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.mirea.infinitejourneysbackend.validation.constraints.ValidFiles;

import java.util.List;

import static java.util.Objects.isNull;

public class FilesValidator implements ConstraintValidator<ValidFiles, List<MultipartFile>> {

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext constraintValidatorContext) {
        if (isNull(files)) {
            return true;
        }

        // Отключение дефолтного вывода об ошибках
        constraintValidatorContext.disableDefaultConstraintViolation();

        return files.stream().allMatch(file -> {
                    String fileName = file.getOriginalFilename();
                    // Проверка имени файла
                    if (isNull(fileName) || isAllowedName(fileName)) {
                        constraintValidatorContext.buildConstraintViolationWithTemplate("Имя файла не может быть пустым '" + fileName + "'").addConstraintViolation();
                        return false;
                    }
                    // Проверка расширения файла
                    String extension = StringUtils.getFilenameExtension(fileName);
                    if (!isAllowedExtension(extension)) {
                        constraintValidatorContext.buildConstraintViolationWithTemplate("Неподдерживаемый формат файла '" + extension + "'").addConstraintViolation();
                        return false;
                    }
                    return true;
                }
        );
    }

    // Только картинки и видео
    private boolean isAllowedExtension(String extension) {
        if (!isNull(extension)) {
            return List.of("png", "jpg", "jpeg", "gif", "bmp", "svg", "webp", "mp4", "avi", "mov")
                    .contains(extension.toLowerCase());
        }
        return false;
    }

    private boolean isAllowedName(String fileName) {
        return StringUtils.stripFilenameExtension(fileName).isEmpty();
    }

}

