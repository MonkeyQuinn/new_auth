package org.example.new_auth.service.auth;

import org.example.new_auth.batch.BatchResult;
import org.example.new_auth.domain.Permission;
import org.example.new_auth.domain.User;

import java.util.List;

public interface AuthService {

    User getUserByUsername(String username);

    BatchResult<User> findUsersByUsernames(List<String> usernames);

    BatchResult<User> findUsersByIds(List<Long> ids);

    BatchResult<String> filterUsernamesByAreas(List<String> usernames, List<String> areas);

    BatchResult<Long> filterUserIdsByAreas(List<Long> ids, List<String> areas);

    BatchResult<Long> filterUserIdsByOperations(List<Long> ids, List<String> operations);

    BatchResult<String> extractAreas(List<String> usernames);

    BatchResult<String> extractOperations(List<String> usernames);

    List<User> filterUsersByUsernames(List<User> domainList, List<String> usernames);

    List<String> extractAreasFromUsers(List<User> domainList);

    List<String> extractOperationsFromUsers(List<User> domainList);

    BatchResult<User> saveUsers(List<User> domainList);

    BatchResult<User> grantPermissions(List<String> usernames, List<Permission> domainList);

    BatchResult<User> revokeAreas(List<String> usernames, List<String> areas);

    BatchResult<User> revokeOperations(List<String> usernames, List<String> operations);

    BatchResult<User> clearPermissions(List<String> usernames);

}
