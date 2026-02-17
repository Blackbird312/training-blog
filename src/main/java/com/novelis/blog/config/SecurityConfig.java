package com.novelis.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private static final String API_URI = "/api/v1";
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (lecture)
                .requestMatchers(HttpMethod.GET, API_URI + "/articles/**").permitAll()

                // Comments: lecture public, ajout nécessite login
                .requestMatchers(HttpMethod.GET, API_URI + "/articles/*/comments/**").permitAll()
                .requestMatchers(HttpMethod.POST, API_URI + "/articles/*/comments/**").authenticated()

                // Articles: création / modification réservée
                .requestMatchers(HttpMethod.POST, API_URI + "/articles/**").hasAnyRole("AUTHOR", "ADMIN")
                .requestMatchers(HttpMethod.PUT, API_URI + "/articles/**").hasAnyRole("AUTHOR", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, API_URI + "/articles/**").hasRole("ADMIN")

                // Tout le reste
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))
            );

        return http.build();
    }

    @Bean
    Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthConverter() {
        return (Jwt jwt) -> {
            var authorities = KeycloakRoleExtractor.extractAuthorities(jwt, "blog-api");
            return new JwtAuthenticationToken(jwt, authorities);
        };
    }
}
