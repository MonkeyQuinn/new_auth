package org.example.new_auth.client;

import org.example.new_auth.exception.ApiException;
import org.example.new_auth.model.domain.Login;
import org.example.new_auth.model.domain.Token;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TokenManager {

    private final AuthClient authClient;
    private volatile Token accessToken;


    public TokenManager(AuthClient authClient) {
        this.authClient = authClient;
    }

    public <T> T withTokenRetryOnce(Function<String, T> call) {
        try {
            return call.apply(getAccessToken());

        } catch (ApiException e) {
            if (e.getStatus() == HttpStatus.UNAUTHORIZED || e.getStatus() == HttpStatus.FORBIDDEN) {
                synchronized (this) {
                    refresh();
                }

                return call.apply(getAccessToken());
            }

            throw e;
        }
    }

    private String getAccessToken() {
        if (tokenIsInvalid()) {
            synchronized (this) {
                if (tokenIsInvalid()) {
                    refresh();
                    if (tokenIsInvalid()) throw new IllegalStateException("Token refresh failed");
                }
            }
        }

        return accessToken.getHash();
    }

    private void refresh() {
        Login login = authClient.login();
        this.accessToken = login.getAccess();
    }

    private boolean tokenIsInvalid() {
        return accessToken == null
                || accessToken.getHash() == null
                || accessToken.getHash().isBlank()
                || accessToken.isExpired();
    }

}
