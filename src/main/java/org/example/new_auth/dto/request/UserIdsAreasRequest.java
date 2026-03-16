package org.example.new_auth.dto.request;

import java.util.List;

public record UserIdsAreasRequest(List<Long> ids, List<String> areas) {
}
