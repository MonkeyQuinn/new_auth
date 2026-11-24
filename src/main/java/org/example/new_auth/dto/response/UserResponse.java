package org.example.new_auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserResponse(Long id, Integer clientId, Byte status, Boolean passChangeNeeded, Boolean twoFactorEnabled,
                           Boolean inheritClient, String firstname, String lastname, Integer refreshTokenTtl,
                           Integer accessTokenTtl, Integer tokenCount, List<String> usernames,
                           List<PermissionResponse> permissions, List<RoleResponse> roles, String password) {
}
