package org.example.new_auth.mapper;

import org.example.new_auth.domain.Token;
import org.example.new_auth.external.response.ExternalTokenResponse;
import org.springframework.stereotype.Component;

@Component
public class TokenMapper {

    public Token fromResponse(ExternalTokenResponse response) {
        return new Token(
                response.hash(),
                response.expiresAt(),
                response.ttl(),
                response.issueDate()
        );
    }

}
