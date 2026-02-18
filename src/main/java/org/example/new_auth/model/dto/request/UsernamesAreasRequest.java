package org.example.new_auth.model.dto.request;

import java.util.List;

public record UsernamesAreasRequest(List<String> usernames, List<String> areas) {
}
