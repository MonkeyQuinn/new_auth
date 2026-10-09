package org.example.new_auth.service.auth;

import org.example.new_auth.batch.BatchResult;
import org.example.new_auth.domain.Permission;
import org.example.new_auth.domain.User;
import org.example.new_auth.dto.request.UserIdProductIdsRequest;
import org.example.new_auth.dto.request.UsernameUserIdRequest;

import java.util.List;

public interface AuthService {

    User getUserByUsername(String name);

    User getUserById(Long id);

    BatchResult<User> getUsersByUsernames(List<String> usernames);

    BatchResult<User> getUsersByIds(List<Long> ids);

    BatchResult<Long> getUserIdsByUsernames(List<String> usernames);

    BatchResult<UsernameUserIdRequest> getUserIdsByUsernamesLinked(List<String> usernames, int pack, int interval);

    BatchResult<String> getAreasByUsernames(List<String> usernames);

    BatchResult<String> getAreasByUserIds(List<Long> ids);

    BatchResult<String> getOperationsByUsernames(List<String> usernames);

    BatchResult<String> getOperationsByUserIds(List<Long> ids);

    List<User> filterUsersByUsernames(List<User> users, List<String> usernames);

    BatchResult<String> filterUsernamesByAreas(List<String> usernames, List<String> areas);

    BatchResult<String> filterUsernamesByOperations(List<String> usernames, List<String> operations);

    BatchResult<Long> filterUserIdsByAreas(List<Long> ids, List<String> areas);

    BatchResult<Long> filterUserIdsByOperations(List<Long> ids, List<String> operations);

    List<String> extractAreas(List<User> users);

    List<String> extractOperations(List<User> users);

    BatchResult<User> saveUsers(List<User> users);

    BatchResult<User> grantPermissionsByUsernames(List<String> usernames, List<Permission> permissions, int pack, int interval);

    BatchResult<User> grantPermissionsByUserIds(List<Long> ids, List<Permission> permissions);

    BatchResult<User> grantOldProductsByUserIds(List<UserIdProductIdsRequest> userIdsProductIds, int pack, int interval);

    BatchResult<User> revokeAreasByUsernames(List<String> usernames, List<String> areas, int pack, int interval);

    BatchResult<User> revokeOperationsByUsernames(List<String> usernames, List<String> operations, int pack, int interval);

    BatchResult<User> revokeAreasByUserIds(List<Long> ids, List<String> areas);

    BatchResult<User> revokeOperationsByUserIds(List<Long> ids, List<String> operations);

    BatchResult<User> clearPermissionsByUsernames(List<String> usernames, int pack, int interval);

    BatchResult<User> clearPermissionsByUserIds(List<Long> ids);

}
