package org.example.new_auth.model.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String message, String uri, String path, LocalDateTime timestamp) {
}
