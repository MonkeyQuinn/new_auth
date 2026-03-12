package org.example.new_auth.service.impl;

import org.example.new_auth.client.AuthClient;
import org.example.new_auth.client.TokenManager;
import org.example.new_auth.model.domain.Permission;
import org.example.new_auth.model.domain.User;
import org.example.new_auth.service.UserQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.new_auth.util.UserUtils.*;

@Service
public class UserQueryServiceImp implements UserQueryService {

    private final AuthClient authClient;
    private final TokenManager tokenManager;

    public UserQueryServiceImp(AuthClient authClient, TokenManager tokenManager) {
        this.authClient = authClient;
        this.tokenManager = tokenManager;
    }

    @Override
    public User getUserById(Long id) {
        return tokenManager.withTokenRetryOnce(token -> authClient.getUser(id, token));
    }

    @Override
    public User getUserByUsername(String username) {
        return tokenManager.withTokenRetryOnce(token -> authClient.getUser(username, token));
    }

    @Override
    public User saveUser(User user) {
        return tokenManager.withTokenRetryOnce(token -> authClient.saveUser(user, token));
    }

    @Override
    public List<User> filterUsersByUsernames(List<User> users, List<String> usernames) {
        Set<String> rawRequestedUsernames = ofNullableStream(usernames).collect(Collectors.toSet());

        return nonNullStream(users)
                .filter(user ->
                        ofNullableStream(user.getUsernames())
                                .anyMatch(rawRequestedUsernames::contains)
                ).toList();
    }

    @Override
    public List<String> extractAreasFromUsers(List<User> users) {
        return extractUniqueFromUsers(users, Permission::area);
    }

    @Override
    public List<String> extractOperationsFromUsers(List<User> users) {
        return extractUniqueFromUsers(users, Permission::operation);
    }

}
