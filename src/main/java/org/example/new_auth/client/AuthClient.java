package org.example.new_auth.client;

import org.example.new_auth.config.ExternalApiProperties;
import org.example.new_auth.mapper.LoginMapper;
import org.example.new_auth.mapper.UserMapper;
import org.example.new_auth.model.domain.Login;
import org.example.new_auth.model.domain.User;
import org.example.new_auth.model.external.response.ExternalLoginResponse;
import org.example.new_auth.model.external.response.ExternalUserResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
public class AuthClient {

    private final RestClient restClient;
    private final ExternalApiProperties properties;
    private final UserMapper userMapper;
    private final LoginMapper loginMapper;

    public AuthClient(RestClient restClient, ExternalApiProperties properties,
                      UserMapper userMapper, LoginMapper loginMapper) {
        this.restClient = restClient;
        this.properties = properties;
        this.userMapper = userMapper;
        this.loginMapper = loginMapper;
    }

    public Login login() {
        return loginMapper.toDomain(exchangePostRequest(
                ExternalLoginResponse.class,
                properties.endpoints().login(),
                loginHeaders(),
                new Object()
        ));
    }

    public User getUser(String username, String token) {
        return userMapper.toDomain(exchangeGetRequest(
                ExternalUserResponse.class,
                properties.endpoints().username(),
                clientHeaders(token),
                Map.of("username", username)
        ));
    }

    public User getUser(Long userId, String token) {
        return userMapper.toDomain(exchangeGetRequest(
                ExternalUserResponse.class,
                properties.endpoints().userId(),
                clientHeaders(token),
                Map.of("id", String.valueOf(userId))
        ));
    }

    public User saveUser(User user, String token) {
        return userMapper.toDomain(exchangePostRequest(
                ExternalUserResponse.class,
                properties.endpoints().save(),
                clientHeaders(token),
                userMapper.toExternal(user)
        ));
    }

    private <T> T exchangePostRequest(Class<T> responseType, String endpoint,
                                      Map<String, String> headers, Object body) {
        return restClient.post()
                .uri(uriBuilder(endpoint, Map.of()))
                .headers(httpHeaders -> headers.forEach(httpHeaders::set))
                .body(body)
                .retrieve()
                .body(responseType);
    }

    private <T> T exchangeGetRequest(Class<T> responseType, String endpoint,
                                     Map<String, String> headers, Map<String, String> params) {
        return restClient.get()
                .uri(uriBuilder(endpoint, params))
                .headers(httpHeaders -> headers.forEach(httpHeaders::set))
                .retrieve()
                .body(responseType);
    }

    private URI uriBuilder(String endpoint, Map<String, String> params) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(endpoint);
        params.forEach(uriBuilder::queryParam);
        return uriBuilder.build().toUri();
    }

    private Map<String, String> loginHeaders() {
        return Map.of(
                "Authorization",
                "Basic " + encodeCredentials(properties.userName(), properties.userPass()),
                "Client-Authorization",
                "Basic " + encodeCredentials(properties.clientName(), properties.clientPass()));
    }

    private Map<String, String> clientHeaders(String token) {
        return Map.of(
                "Authorization",
                "Bearer " + token,
                "Client-Authorization",
                "Basic " + encodeCredentials(properties.clientName(), properties.clientPass()));
    }

    private String encodeCredentials(String username, String password) {
        return Base64.getEncoder().encodeToString(
                String.format("%s:%s", username, password).getBytes(StandardCharsets.UTF_8));
    }

}
