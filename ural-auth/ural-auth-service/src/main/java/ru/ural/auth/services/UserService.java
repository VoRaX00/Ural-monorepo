package ru.ural.auth.services;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ural.auth.entities.Role;
import ru.ural.auth.entities.User;
import ru.ural.enums.UserRole;
import ru.ural.auth.mappers.UserMapper;
import ru.ural.auth.models.UserModel;
import ru.ural.auth.repositories.RoleRepository;
import ru.ural.auth.repositories.UserRepository;
import ru.ural.auth.utils.AuthUtils;
import ru.ural.exceptions.ConflictException;
import ru.ural.exceptions.InternalServerException;
import ru.ural.exceptions.UnauthorizedException;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CredentialService credentialService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    @NonNull
    public User findUser(@NonNull String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UnauthorizedException("Invalid login or password"));
    }

    public void checkDuplicate(@NonNull UserModel user) {
        boolean existsDuplicate = userRepository.existsByLogin(user.getLogin());
        if (existsDuplicate) {
            throw new ConflictException(String.format(
                    "User with login %s - already exists",
                    user.getLogin()
            ));
        }
    }

    @NonNull
    @Transactional
    public User createUser(@NonNull UserModel userModel) {
        User user = userMapper.toEntity(userModel);

        Role userRole = roleRepository.findByCode(UserRole.USER)
                .orElseThrow(() -> new InternalServerException("Not found role: USER"));

        user.setRoles(Set.of(userRole));

        String salt = AuthUtils.generateSalt();
        String hashPassword = AuthUtils.hashPassword(userModel.getPassword(), salt);

        var savedUser = userRepository.save(user);

        credentialService.saveCredentials(user, hashPassword, salt);
        return savedUser;
    }

}
