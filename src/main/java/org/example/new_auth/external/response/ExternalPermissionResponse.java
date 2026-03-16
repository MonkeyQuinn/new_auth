package org.example.new_auth.external.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalPermissionResponse(String area, String topic, String operation) {
}
