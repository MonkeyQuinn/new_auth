package org.example.new_auth.service;

import org.example.new_auth.model.domain.Permission;
import org.example.new_auth.model.domain.User;
import org.example.new_auth.model.dto.response.batch.BatchResult;

import java.util.List;

public interface BatchService {

    BatchResult<User> findUsersByUsernames(List<String> usernames);

    BatchResult<User> findUsersByIds(List<Long> ids);

    BatchResult<User> grantPermissions(List<String> usernames, List<Permission> permissions);

    BatchResult<User> revokeAreas(List<String> usernames, List<String> areas);

    BatchResult<User> revokeOperations(List<String> usernames, List<String> operations);

    BatchResult<User> clearPermissions(List<String> usernames);

    BatchResult<String> filterUsernamesByAreas(List<String> usernames, List<String> areas);

    BatchResult<Long> filterUserIdsByAreas(List<Long> ids, List<String> areas);

    BatchResult<Long> filterUserIdsByOperations(List<Long> ids, List<String> operations);

    BatchResult<String> extractAreasByUsernames(List<String> usernames);

    BatchResult<String> extractOperationsByUsernames(List<String> usernames);

    BatchResult<User> saveUsers(List<User> users);

}
