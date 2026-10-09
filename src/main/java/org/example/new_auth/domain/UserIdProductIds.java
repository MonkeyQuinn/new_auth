package org.example.new_auth.domain;

import java.util.List;

public record UserIdProductIds(Long userId, List<Integer> productIds) {
}
