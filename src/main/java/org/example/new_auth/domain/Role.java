package org.example.new_auth.domain;

import java.util.List;

public record Role(Long id, String code, String description, Integer organization, List<Permission> permissions) {

    public Role {
        permissions = permissions == null ? List.of() : List.copyOf(permissions);
    }

}
