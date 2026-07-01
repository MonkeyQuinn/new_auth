package org.example.new_auth.service.auth;

import org.example.new_auth.batch.BatchResult;
import org.example.new_auth.domain.Permission;
import org.example.new_auth.domain.User;

import java.util.List;

public interface AuthService {

    User getUserByUsername(String name);

    User getUserById(Long id);

    BatchResult<User> getUsersByUsernames(List<String> usernames);

    BatchResult<User> getUsersByIds(List<Long> ids);

    BatchResult<String> filterUsernamesByAreas(List<String> usernames, List<String> areas);

    BatchResult<Long> filterUserIdsByAreas(List<Long> ids, List<String> areas);

    BatchResult<Long> filterUserIdsByOperations(List<Long> ids, List<String> operations);

    BatchResult<Long> getUserIdsByUsernames(List<String> usernames);

    BatchResult<String> getAreasByUsernames(List<String> usernames);

    BatchResult<String> getOperationsByUsernames(List<String> usernames);

    List<User> filterUsersByUsernames(List<User> domainList, List<String> usernames);

    List<String> extractAreas(List<User> domainList);

    List<String> extractOperations(List<User> domainList);

    BatchResult<User> saveUsers(List<User> domainList);

    BatchResult<User> grantPermissionsByUsernames(List<String> usernames, List<Permission> permissions);

    BatchResult<User> grantPermissionsByUserIds(List<Long> ids, List<Permission> permissions);

    BatchResult<User> revokeAreasByUsernames(List<String> usernames, List<String> areas);

    BatchResult<User> revokeOperationsByUsernames(List<String> usernames, List<String> operations);

    BatchResult<User> revokeAreasByUserIds(List<Long> ids, List<String> areas);

    BatchResult<User> clearPermissionsByUsernames(List<String> usernames);

}
