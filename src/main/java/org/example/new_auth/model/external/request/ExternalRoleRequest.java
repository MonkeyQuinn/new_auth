package org.example.new_auth.model.external.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalRoleRequest(Long id, String code, String description, Integer organization,
                                  List<ExternalPermissionRequest> permissions) {
}
