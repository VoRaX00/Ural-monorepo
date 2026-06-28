package ru.ural.users.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ural.auth.dto.AuthDto;
import ru.ural.auth.dto.UserDto;
import ru.ural.users.entities.Avatar;
import ru.ural.users.entities.User;
import ru.ural.exceptions.ConflictException;
import ru.ural.exceptions.NotFoundException;
import ru.ural.users.mappers.AvatarMapper;
import ru.ural.users.mappers.UserMapper;
import ru.ural.users.models.AvatarModel;
import ru.ural.users.models.RegistrationModel;
import ru.ural.users.models.UserModel;
import ru.ural.users.repositories.AvatarRepository;
import ru.ural.users.repositories.UserRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private static final String ERROR_USER_EXISTS = "Пользователь с таким email или номером телефона уже существует";

    private static final String USER_NOT_FOUND = "Пользователь с uuid: %s не найден";

    private final AuthSender authSender;

    private final UserRepository userRepository;

    private final AvatarRepository avatarRepository;

    private final UserMapper userMapper;

    private final AvatarMapper avatarMapper;

    private final UserRatingService userRatingService;

    @Transactional
    public AuthDto create(@NonNull RegistrationModel model) {
        validateUser(model);

        User newUser = userMapper.toEntity(model);
        User savedUser = userRepository.save(newUser);
        userRatingService.createDefaultRating(savedUser.getUuid());
        return sendRegistration(model, savedUser.getUuid());
    }

    private AuthDto sendRegistration(RegistrationModel user, UUID uuid) {
        UserDto userDto = userMapper.toAuthUserDto(user);
        userDto.setUuid(uuid);

        return authSender.registration(userDto);
    }

    @Transactional
    public UserModel updateUser(@NonNull String uuid, @NonNull UserModel userModel) {
        User user = userRepository.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new NotFoundException(String.format(
                        USER_NOT_FOUND, uuid
                )));

        validateUserForUpdate(userModel, uuid);
        updateAvatar(user, userModel.getAvatar());

        userMapper.mapModelToEntity(user, userModel);
        return enrichWithRating(userMapper.toModel(user));
    }

    private void validateUser(UserModel userModel) {
        boolean existsUser = userRepository.existsByEmailOrPhoneNumber(
                userModel.getEmail(),
                userModel.getPhoneNumber()
        );

        if (existsUser) {
            throw new ConflictException(ERROR_USER_EXISTS);
        }
    }

    private void validateUserForUpdate(UserModel userModel, String uuid) {
        UUID castUuid = uuid == null
                ? null
                : UUID.fromString(uuid);

        boolean existsUser = userRepository.existsByEmailOrPhoneNumber(
                userModel.getEmail(),
                userModel.getPhoneNumber(),
                castUuid
        );

        if (existsUser) {
            throw new ConflictException(ERROR_USER_EXISTS);
        }
    }

    public UserModel getByUuid(String uuid) {
        User user = userRepository.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new NotFoundException(String.format(
                        USER_NOT_FOUND, uuid
                )));

        return enrichWithRating(userMapper.toModel(user));
    }

    private void updateAvatar(User user, AvatarModel avatarModel) {
        if (avatarModel == null) {
            return;
        }

        Avatar avatar = user.getAvatar();
        if (avatar != null) {
            avatarMapper.mapModelToEntity(avatar, avatarModel);
            return;
        }

        Avatar updatedAvatar = avatarMapper.toEntity(avatarModel);
        updatedAvatar.setUser(user);

        var savedAvatar = avatarRepository.save(updatedAvatar);
        user.setAvatar(savedAvatar);
    }

    private UserModel enrichWithRating(UserModel userModel) {
        var rating = userRatingService.getOrDefault(userModel.getUuid());
        userModel.setAverageRating(rating.getAverageRating());
        userModel.setRatingsCount(rating.getRatingsCount());
        return userModel;
    }

}
