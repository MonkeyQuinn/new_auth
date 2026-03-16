package org.example.new_auth.domain;

import java.time.OffsetDateTime;

public record Token(String hash, OffsetDateTime expiresAt, Integer ttl, OffsetDateTime issueDate) {

    public boolean isExpired() {
        return expiresAt == null
                || OffsetDateTime.now().isAfter(expiresAt.minusSeconds(30));
    }

}
