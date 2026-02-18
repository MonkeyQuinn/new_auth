package org.example.new_auth.model.dto.request;

import java.util.List;

public record UsernamesPermissionsRequest(List<String> usernames, List<PermissionRequest> permissions) {
}
