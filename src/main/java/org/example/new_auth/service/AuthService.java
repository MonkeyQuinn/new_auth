package org.example.new_auth.service;

import org.example.new_auth.model.domain.Permission;
import org.example.new_auth.model.domain.User;
import org.example.new_auth.model.dto.response.batch.BatchResult;

import java.util.List;

public interface AuthService {

    User getUserById(Long id);

    User getUserByUsername(String username);

    BatchResult<User> findUsersByUsernames(List<String> usernames);

    BatchResult<User> findUsersByIds(List<Long> ids);

    BatchResult<String> findUsernamesByRequiredAreas(List<String> usernames, List<String> areas);

    BatchResult<Long> findUserIdsByRequiredAreas(List<Long> ids, List<String> areas);

    BatchResult<Long> findUserIdsByRequiredOperations(List<Long> ids, List<String> operations);

    BatchResult<String> extractAreasByUsernames(List<String> usernames);

    BatchResult<String> extractOperationsByUsernames(List<String> usernames);

    List<User> filterUsersByUsernames(List<User> users, List<String> usernames);

    List<String> extractAreasFromUsers(List<User> users);

    List<String> extractOperationsFromUsers(List<User> users);

    User saveUser(User user);

    BatchResult<User> saveUsers(List<User> users);

    BatchResult<User> grantPermissions(List<String> usernames, List<Permission> permissions);

    BatchResult<User> revokeAreas(List<String> usernames, List<String> areas);

    BatchResult<User> revokeOperations(List<String> usernames, List<String> operations);

}
