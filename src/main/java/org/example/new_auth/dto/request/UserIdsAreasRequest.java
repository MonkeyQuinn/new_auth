package org.example.new_auth.dto.request;

import java.util.List;

public record UserIdsAreasRequest(List<Long> userIds, List<String> areas) {
}
