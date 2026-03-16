package org.example.new_auth.dto.response;

import java.util.List;

public record RoleResponse(Long id, String code, String description, Integer organization,
                           List<PermissionResponse> permissions) {
}
