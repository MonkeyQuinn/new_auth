package org.example.new_auth.service.batch;

import org.example.new_auth.domain.Permission;
import org.example.new_auth.domain.User;
import org.example.new_auth.batch.BatchResult;

import java.util.List;

public interface BatchService {

    BatchResult<User> getUsersByUsernames(List<String> usernames);

    BatchResult<User> getUsersByIds(List<Long> ids);

    BatchResult<Long> getUserIdsByUsernames(List<String> usernames);

    BatchResult<String> getAreasByUsernames(List<String> usernames);

    BatchResult<String> getAreasByUserIds(List<Long> ids);

    BatchResult<String> getOperationsByUsernames(List<String> usernames);

    BatchResult<String> getOperationsByUserIds(List<Long> ids);

    BatchResult<String> filterUsernamesByAreas(List<String> usernames, List<String> areas);

    BatchResult<String> filterUsernamesByOperations(List<String> usernames, List<String> operations);

    BatchResult<Long> filterUserIdsByAreas(List<Long> ids, List<String> areas);

    BatchResult<Long> filterUserIdsByOperations(List<Long> ids, List<String> operations);

    BatchResult<User> saveUsers(List<User> users);

    BatchResult<User> grantPermissionsByUsernames(List<String> usernames, List<Permission> permissions);

    BatchResult<User> grantPermissionsByUserIds(List<Long> ids, List<Permission> permissions);

    BatchResult<User> revokeAreasByUsernames(List<String> usernames, List<String> areas);

    BatchResult<User> revokeOperationsByUsernames(List<String> usernames, List<String> operations);

    BatchResult<User> revokeAreasByUserIds(List<Long> ids, List<String> areas);

    BatchResult<User> revokeOperationsByUserIds(List<Long> ids, List<String> operations);

    BatchResult<User> clearPermissionsByUsernames(List<String> usernames);

    BatchResult<User> clearPermissionsByUserIds(List<Long> ids);

}
