package ru.mirea.infinitejourneysbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mirea.infinitejourneysbackend.config.security.SuperUserConfig;
import ru.mirea.infinitejourneysbackend.domain.dto.user.DeleteUserRequest;
import ru.mirea.infinitejourneysbackend.exception.user.ForbiddenAccessProblem;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class DeleteService {
    private final UserService userService;
    private final TourService tourService;
    private final CommentService commentService;
    private final SuperUserConfig superUserConfig;

    public void deleteUser(DeleteUserRequest request) {
        // Проверяем если права на удаление пользователя
        var currentUser = userService.getCurrentUser();
        var userForDelete = userService.getById(request.userId());

        // Если это не сам пользователь, проверяем
        if (!currentUser.getId().equals(userForDelete.getId())) {

            // Если не админ, то кидаем ошибку
            if (!currentUser.isAdmin()) {
                throw new ForbiddenAccessProblem();
            }

            // Если админ, то проверяем, что удаляемый пользователь не админ
            if (!userService.hasAccessToUser(currentUser, userForDelete)) {
                throw new ForbiddenAccessProblem();
            }
        }

        // Суперпользователь не может удалиться
        if (superUserConfig.isSuperuser(userForDelete.getId())) {
            throw new ForbiddenAccessProblem();
        }

        if (request.deleteComments()) {
            commentService.deleteByAuthorId(userForDelete.getId());
            userForDelete.setComments(null);
        }

        tourService.deleteBySellerId(userForDelete.getId());
        userForDelete.setTours(null);

        userForDelete.setDeletedAt(OffsetDateTime.now());
        userService.save(userForDelete);
    }

}
