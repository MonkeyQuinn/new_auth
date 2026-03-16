package org.example.new_auth.dto.response;

import java.util.List;

public record UserIdNamesResponse(Long id, List<String> usernames) {
}
