package org.example.new_auth.dto.response;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MultiUsernameUserResponse(String matchedUsername, Long userId, List<String> usernames) {
}
