package org.example.new_auth.model.external.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ExternalUserResponse(Long id, Integer clientId, Byte status, Boolean passChangeNeeded,
                                   Boolean twoFactorEnabled, Boolean inheritClient, String firstname, String lastname,
                                   Integer refreshTokenTtl, Integer accessTokenTtl, Integer tokenCount,
                                   List<String> usernames, List<ExternalPermissionResponse> permissions,
                                   List<ExternalRoleResponse> roles) {
}
