package ru.ural.auth.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ural.auth.entities.RefreshToken;
import ru.ural.auth.entities.Role;
import ru.ural.auth.entities.User;
import ru.ural.enums.ClaimKey;
import ru.ural.enums.UserRole;
import ru.ural.auth.models.AuthModel;
import ru.ural.models.UserPrincipals;
import ru.ural.auth.properties.AuthProperty;
import ru.ural.auth.repositories.RefreshTokenRepository;
import ru.ural.exceptions.UnauthorizedException;
import ru.ural.services.JwtVerifier;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final AuthProperty authProperty;

    private final JwtVerifier jwtVerifier;

    @NonNull
    @Transactional
    public AuthModel refreshTokens(String refreshToken) {
        Claims claims = jwtVerifier.verify(refreshToken);
        String refreshJti = claims.getId();

        RefreshToken oldRefreshToken = refreshTokenRepository.findByRefreshJti(refreshJti)
                .orElseThrow(() -> new UnauthorizedException("Not found refresh token"));

        User user = oldRefreshToken.getUser();

        refreshTokenRepository.deleteByRefreshJti(refreshJti);
        return issueToken(user);
    }

    @Transactional
    public void deleteRefreshTokenByAccess(Jwt jwt) {
        String accessJti = jwt.getId();
        refreshTokenRepository.deleteByAccessJti(accessJti);
    }

    @Transactional
    public void deleteAllRefreshTokensByAccess(Jwt jwt) {
        String userUuid = jwt.getClaimAsString(ClaimKey.USER_UUID_KEY.getKey());
        refreshTokenRepository.deleteAllByUserUuid(UUID.fromString(userUuid));
    }

    @NonNull
    @Transactional
    public AuthModel issueToken(@NonNull User user) {
        UserPrincipals userPrincipals = buildPrincipals(user);

        String accessJti = UUID.randomUUID().toString();
        String refreshJti = UUID.randomUUID().toString();

        var now = ZonedDateTime.now();
        ZonedDateTime expiredAtRefreshToken = now.plusSeconds(authProperty.getRefreshTokenExpiredOnSeconds());
        String accessToken = generateAccessToken(userPrincipals, accessJti, now);
        String refreshToken = generateRefreshToken(userPrincipals, refreshJti, now, expiredAtRefreshToken);

        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .issuedAt(now)
                .accessJti(accessJti)
                .refreshJti(refreshJti)
                .user(user)
                .expiredAt(expiredAtRefreshToken)
                .build();

        refreshTokenRepository.save(token);
        return AuthModel.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @NonNull
    private UserPrincipals buildPrincipals(@NonNull User user) {
        Set<UserRole> roles = user.getRoles().stream()
                .map(Role::getCode)
                .collect(Collectors.toSet());

        return UserPrincipals.builder()
                .uuid(user.getUuid().toString())
                .roles(roles)
                .build();
    }

    @NonNull
    private String generateAccessToken(
            @NonNull UserPrincipals principals,
            @NonNull String jti,
            @NonNull ZonedDateTime issueAt
    ) {
        ZonedDateTime expiredAt = issueAt.plusSeconds(authProperty.getAccessTokenExpiredOnSeconds());
        return Jwts.builder()
                .setId(jti)
                .setSubject(principals.getUuid())
                .setIssuedAt(Date.from(issueAt.toInstant()))
                .setExpiration(Date.from(expiredAt.toInstant()))
                .claim(ClaimKey.USER_UUID_KEY.getKey(), principals.getUuid())
                .claim(ClaimKey.ROLES_KEY.getKey(), principals.getRoles())
                .signWith(jwtVerifier.getPrivateKey())
                .compact();
    }

    @NonNull
    private String generateRefreshToken(
            @NonNull UserPrincipals principals,
            @NonNull String jti,
            @NonNull ZonedDateTime issueAt,
            @NonNull ZonedDateTime expiredAt
    ) {
        return Jwts.builder()
                .setId(jti)
                .setSubject(principals.getUuid())
                .setIssuedAt(Date.from(issueAt.toInstant()))
                .setExpiration(Date.from(expiredAt.toInstant()))
                .claim(ClaimKey.USER_UUID_KEY.getKey(), principals.getUuid())
                .signWith(jwtVerifier.getPrivateKey())
                .compact();
    }

}
