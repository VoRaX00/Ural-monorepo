package ru.ural.auth.services;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ural.auth.entities.Credential;
import ru.ural.auth.entities.User;
import ru.ural.auth.models.AuthModel;
import ru.ural.auth.models.LoginModel;
import ru.ural.auth.models.UserModel;
import ru.ural.utils.JwtUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final CredentialService credentialService;

    private final TokenService tokenService;

    public AuthModel login(@NonNull LoginModel loginModel) {
        User user = userService.findUser(loginModel.getLogin());
        Credential credential = credentialService.findByUserUuid(user.getUuid());

        credentialService.checkPassword(loginModel.getPassword(), credential);
        return tokenService.issueToken(user);
    }

    @Transactional
    public AuthModel registration(@NonNull UserModel userModel) {
        userService.checkDuplicate(userModel);
        var user = userService.createUser(userModel);
        return tokenService.issueToken(user);
    }

    @Transactional
    public AuthModel refreshTokens(@NonNull String refreshToken) {
        return tokenService.refreshTokens(refreshToken);
    }

    @Transactional
    public void logout() {
        Authentication authentication = JwtUtils.getAuthentication();
        Jwt accessToken = JwtUtils.getToken(authentication);
        tokenService.deleteRefreshTokenByAccess(accessToken);
    }

    @Transactional
    public void logoutAll() {
        Authentication authentication = JwtUtils.getAuthentication();
        Jwt accessToken = JwtUtils.getToken(authentication);
        tokenService.deleteAllRefreshTokensByAccess(accessToken);
    }

}
