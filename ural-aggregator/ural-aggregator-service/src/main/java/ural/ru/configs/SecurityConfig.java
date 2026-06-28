package ural.ru.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.ural.configs.AllowedUrls;
import ru.ural.decoders.NoVerifyJwtDecoder;
import ru.ural.filters.ExceptionFilterHandler;
import ural.ru.properties.FrontProperty;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AllowedUrls allowedUrls;

    private final ExceptionFilterHandler exceptionFilterHandler;

    private final NoVerifyJwtDecoder noVerifyJwtDecoder;

    private final Optional<JwtDecoder> verifyJwtDecoder;

    private final FrontProperty frontProperty;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter,
            AuthenticationEntryPoint authenticationEntryPoint
    ) throws Exception {
        JwtDecoder decoder = verifyJwtDecoder.orElseGet(() -> {
            log.error("Using NoVerifyJwtDecoder");
            return noVerifyJwtDecoder;
        });

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(resourceServerConfigurer ->
                        resourceServerConfigurer
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .jwt(jwtConfigurer ->
                                    jwtConfigurer
                                            .decoder(decoder)
                                            .jwtAuthenticationConverter(jwtAuthenticationConverter)))
                .authorizeHttpRequests(this::authorizeHttpRequests)
                .addFilterBefore(exceptionFilterHandler, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                frontProperty.getUrl()
        ));

        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "PATCH"
        ));

        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of(
                "Authorization"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    private void authorizeHttpRequests(
            AbstractRequestMatcherRegistry<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl> auth
    ) {
        auth.requestMatchers(allowedUrls.allowedUrls()).permitAll()
                .anyRequest()
                .authenticated();
    }

}
