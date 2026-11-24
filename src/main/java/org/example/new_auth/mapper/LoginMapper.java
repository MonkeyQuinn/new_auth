package org.example.new_auth.mapper;

import org.example.new_auth.domain.Login;
import org.example.new_auth.external.response.ExternalLoginResponse;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {

    private final TokenMapper tokenMapper;

    public LoginMapper(TokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
    }

    public Login fromResponse(ExternalLoginResponse response) {
        return new Login(
                tokenMapper.fromResponse(response.access()),
                tokenMapper.fromResponse(response.refresh()),
                response.passChangeNeeded()
        );
    }

}
