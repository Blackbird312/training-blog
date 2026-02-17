package com.novelis.blog.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public final class AuthUtils {
    private AuthUtils() {}

    public static String keycloakUserId(Authentication auth) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) auth;
        return token.getToken().getSubject(); // "sub"
    }

    public static String username(Authentication auth) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) auth;
        return token.getToken().getClaimAsString("preferred_username");
    }
}
