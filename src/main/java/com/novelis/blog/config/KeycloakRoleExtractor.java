package com.novelis.blog.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

public final class KeycloakRoleExtractor {

    private KeycloakRoleExtractor() {}

    public static Collection<SimpleGrantedAuthority> extractAuthorities(Jwt jwt, String clientId) {
        Set<String> roles = new HashSet<>();

        // 1) Realm roles: realm_access.roles
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null) {
            Object realmRoles = realmAccess.get("roles");
            if (realmRoles instanceof Collection<?> rr) {
                rr.forEach(r -> roles.add(String.valueOf(r)));
            }
        }

        // 2) Client roles: resource_access[clientId].roles
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null && clientId != null) {
            Object client = resourceAccess.get(clientId);
            if (client instanceof Map<?, ?> cm) {
                Object clientRoles = cm.get("roles");
                if (clientRoles instanceof Collection<?> cr) {
                    cr.forEach(r -> roles.add(String.valueOf(r)));
                }
            }
        }

        // Map to Spring Security authorities: ROLE_X
        return roles.stream()
            .filter(r -> r != null && !r.isBlank())
            .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
            .toList();
    }
}
