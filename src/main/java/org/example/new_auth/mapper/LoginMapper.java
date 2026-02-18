package org.example.new_auth.mapper;

import org.example.new_auth.model.domain.Login;
import org.example.new_auth.model.external.response.ExternalLoginResponse;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {

    private final TokenMapper tokenMapper;

    public LoginMapper(TokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
    }

    public Login toDomain(ExternalLoginResponse response) {
        return new Login(
                tokenMapper.toDomain(response.access()),
                tokenMapper.toDomain(response.refresh()),
                response.passChangeNeeded()
        );
    }

}
