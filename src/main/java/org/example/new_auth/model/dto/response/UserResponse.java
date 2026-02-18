package org.example.new_auth.model.dto.response;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserResponse(Long id, Integer clientId, Byte status, Boolean passChangeNeeded, Boolean twoFactorEnabled,
                           Boolean inheritClient, String firstname, String lastname, Integer refreshTokenTtl,
                           Integer accessTokenTtl, Integer tokenCount, List<String> usernames,
                           List<PermissionResponse> permissions, List<RoleResponse> roles) {
}
