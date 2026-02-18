package org.example.new_auth.model.external.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ExternalUserRequest(Long id, Integer clientId, Byte status, Boolean passChangeNeeded,
                                  Boolean twoFactorEnabled, Boolean inheritClient, String firstname, String lastname,
                                  Integer refreshTokenTtl, Integer accessTokenTtl, Integer tokenCount,
                                  List<String> usernames, List<ExternalPermissionRequest> permissions,
                                  List<ExternalRoleRequest> roles) {
}
