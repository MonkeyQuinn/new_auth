package org.example.new_auth.dto.request;

import java.util.List;

public record UsernamesOperationsRequest(List<String> usernames, List<String> operations) {
}
