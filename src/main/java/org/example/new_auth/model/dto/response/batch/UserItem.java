package org.example.new_auth.model.dto.response.batch;

import org.example.new_auth.model.domain.User;

public record UserItem(String username, User user) {
}
