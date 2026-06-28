package ru.ural.decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import ru.ural.exceptions.UnauthorizedException;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

public class NoVerifyJwtDecoder implements JwtDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Jwt decode(String token) {
        try {
            String[] parts = token.split("\\.");

            if (parts.length < 2) {
                throw new UnauthorizedException("Invalid JWT");
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));

            Map<String, Object> claims = objectMapper.readValue(payloadJson, Map.class);

            Long exp = ((Number) claims.get("exp")).longValue();
            Instant expiresAt = Instant.ofEpochSecond(exp);

            if (expiresAt.isBefore(Instant.now())) {
                throw new UnauthorizedException("Token expired");
            }

            Map<String, Object> headers = Map.of("alg", "none");

            return new Jwt(
                    token,
                    Instant.now(),
                    expiresAt,
                    headers,
                    claims
            );
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token", e);
        }
    }
}
