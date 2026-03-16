package org.example.new_auth.domain;

public record Login(Token access, Token refresh, Boolean passChangeNeeded) {
}
