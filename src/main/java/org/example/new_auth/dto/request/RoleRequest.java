package org.example.new_auth.dto.request;

import java.util.List;

public record RoleRequest(Long id, String code, String description, Integer organization,
                          List<PermissionRequest> permissions) {
}
