package org.example.new_auth.model.external.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalPermissionResponse(String area, String topic, String operation) {
}
