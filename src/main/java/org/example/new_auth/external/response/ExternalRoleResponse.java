package org.example.new_auth.external.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalRoleResponse(Long id, String code, String description, Integer organization,
                                   List<ExternalPermissionResponse> permissions) {
}
