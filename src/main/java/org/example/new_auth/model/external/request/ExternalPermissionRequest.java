package org.example.new_auth.model.external.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalPermissionRequest(String area, String topic, String operation) {
}
