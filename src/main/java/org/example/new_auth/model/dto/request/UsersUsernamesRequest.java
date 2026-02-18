package org.example.new_auth.model.dto.request;

import java.util.List;

public record UsersUsernamesRequest(List<UserRequest> users, List<String> usernames) {
}
