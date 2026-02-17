package com.novelis.blog.service;

import com.novelis.blog.domain.User;
import com.novelis.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserResolverService {

    private final UserRepository userRepository;

    public UUID resolveLocalUserId(Authentication auth) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) auth;

        String keycloakId = token.getToken().getSubject(); // sub
        String email = token.getToken().getClaimAsString("email");
        String username = token.getToken().getClaimAsString("preferred_username");
        String fullName = token.getToken().getClaimAsString("name");

        return userRepository.findByKeycloakId(keycloakId)
            .map(User::getId)
            .orElseGet(() -> {
                User u = User.builder()
                    .id(UUID.randomUUID())
                    .keycloakId(keycloakId)
                    .email(email)
                    .fullName(fullName != null ? fullName : username)
                    .createdAt(OffsetDateTime.now(ZoneOffset.UTC))
                    .build();

                return userRepository.save(u).getId();
            });
    }
}
