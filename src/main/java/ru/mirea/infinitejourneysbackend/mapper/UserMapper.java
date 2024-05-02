package ru.mirea.infinitejourneysbackend.mapper;

import org.mapstruct.Mapper;
import ru.mirea.infinitejourneysbackend.domain.dto.auth.SignUpRequest;
import ru.mirea.infinitejourneysbackend.domain.dto.user.UserProfileResponse;
import ru.mirea.infinitejourneysbackend.domain.dto.user.UserResponse;
import ru.mirea.infinitejourneysbackend.domain.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    UserProfileResponse toProfileResponse(User user);

    List<UserResponse> toResponse(List<User> users);

    User toEntity(SignUpRequest request);
}

