package org.example.new_auth.dto.request;

import java.util.List;

public record UserIdsPermissionsRequest(List<Long> userIds, List<PermissionRequest> permissions) {
}
