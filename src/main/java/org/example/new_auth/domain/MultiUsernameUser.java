package org.example.new_auth.domain;

import java.util.List;

public record MultiUsernameUser(String matchedUsername, Long userId, List<String> usernames) {
}
