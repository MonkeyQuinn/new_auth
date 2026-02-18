package org.example.new_auth.model.dto.request;

import java.util.List;

public record UserIdsOperationsRequest(List<Long> ids, List<String> operations) {
}
