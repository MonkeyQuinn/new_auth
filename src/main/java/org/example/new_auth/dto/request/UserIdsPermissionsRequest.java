package org.example.new_auth.dto.request;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserIdsPermissionsRequest(List<Long> userIds, List<PermissionRequest> permissions) {
}
