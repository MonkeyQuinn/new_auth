package org.example.new_auth.model.domain;

import java.time.OffsetDateTime;

public class Token {

    private String hash;
    private OffsetDateTime expiresAt;
    private Integer ttl;
    private OffsetDateTime issueDate;

    public Token(String hash, OffsetDateTime expiresAt, Integer ttl, OffsetDateTime issueDate) {
        this.hash = hash;
        this.expiresAt = expiresAt;
        this.ttl = ttl;
        this.issueDate = issueDate;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Integer getTtl() {
        return ttl;
    }

    public void setTtl(Integer ttl) {
        this.ttl = ttl;
    }

    public OffsetDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(OffsetDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public boolean isExpired() {
        return expiresAt == null
                || OffsetDateTime.now().isAfter(expiresAt.minusSeconds(30));
    }

}
