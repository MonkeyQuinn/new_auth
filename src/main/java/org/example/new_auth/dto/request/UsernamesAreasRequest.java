package org.example.new_auth.dto.request;

import java.util.List;

public record UsernamesAreasRequest(List<String> usernames, List<String> areas) {
}
