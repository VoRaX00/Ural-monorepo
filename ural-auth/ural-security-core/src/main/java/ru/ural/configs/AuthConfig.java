package ru.ural.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.HandlerExceptionResolver;
import ru.ural.converters.AuthConverter;
import ru.ural.converters.JwtConverter;
import ru.ural.filters.ExceptionFilterHandler;
import ru.ural.decoders.NoVerifyJwtDecoder;
import ru.ural.properties.JwtProperty;
import ru.ural.services.JwtVerifier;
import ru.ural.services.LoggingAuthenticationEntryPoint;
import ru.ural.services.RoleSecurityService;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Configuration
@EnableConfigurationProperties(JwtProperty.class)
public class AuthConfig {

    @Bean
    @Order(LOWEST_PRECEDENCE)
    @ConditionalOnMissingBean
    public AllowedUrls allowedUrls() {
        var allowedUrls = new String[]{
                "/swagger-ui/**",
                "/v3/**",
                "/actuator/**",
                "/api/auth/login",
                "/api/auth/refresh-tokens",
                "/api/auth/registration",
                "/api/users/registration",
                "/auth/login",
                "/auth/refresh-tokens",
                "/auth/registration",
                "/users/registration",
        };

        return () -> allowedUrls;
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter(
            AuthConverter authConverter
    ) {
        return new JwtConverter(authConverter);
    }

    @Bean
    @ConditionalOnMissingBean
    public RoleSecurityService roleSecurityService() {
        return new RoleSecurityService();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthConverter authConverter(RoleSecurityService roleSecurityService) {
        return new AuthConverter(roleSecurityService);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionFilterHandler exceptionFilterHandler(
            @Qualifier("handlerExceptionResolver")
            HandlerExceptionResolver resolver
    ) {
        return new ExceptionFilterHandler(resolver);
    }

    @Bean
    public JwtVerifier jwtVerifier(JwtProperty jwtProperty) {
        return new JwtVerifier(jwtProperty);
    }

    @Bean
    public NoVerifyJwtDecoder noVerifyJwtDecoder() {
        return new NoVerifyJwtDecoder();
    }

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "security.jwt", name = "public-key")
    public JwtDecoder verifiedJwtDecoder(JwtProperty jwtProperty) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperty.getPublicKey());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoggingAuthenticationEntryPoint();
    }

}
