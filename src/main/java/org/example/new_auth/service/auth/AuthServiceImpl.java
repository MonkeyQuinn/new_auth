package org.example.new_auth.service.auth;

import org.example.new_auth.domain.Permission;
import org.example.new_auth.domain.User;
import org.example.new_auth.batch.BatchResult;
import org.example.new_auth.service.batch.BatchService;
import org.example.new_auth.service.user.UserQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final BatchService batchService;
    private final UserQueryService userService;

    public AuthServiceImpl(BatchService batchService, UserQueryService userService) {
        this.batchService = batchService;
        this.userService = userService;
    }

    @Override
    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    @Override
    public BatchResult<User> findUsersByUsernames(List<String> usernames) {
        return batchService.findUsersByUsernames(usernames);
    }

    @Override
    public BatchResult<User> findUsersByIds(List<Long> ids) {
        return batchService.findUsersByIds(ids);
    }

    @Override
    public BatchResult<String> filterUsernamesByAreas(List<String> usernames, List<String> areas) {
        return batchService.filterUsernamesByAreas(usernames, areas);
    }

    @Override
    public BatchResult<Long> filterUserIdsByAreas(List<Long> ids, List<String> areas) {
        return batchService.filterUserIdsByAreas(ids, areas);
    }

    @Override
    public BatchResult<Long> filterUserIdsByOperations(List<Long> ids, List<String> operations) {
        return batchService.filterUserIdsByOperations(ids, operations);
    }

    @Override
    public BatchResult<Long> extractUserIds(List<String> usernames) {
        return batchService.extractUserIdsByUsernames(usernames);
    }

    @Override
    public BatchResult<String> extractAreas(List<String> usernames) {
        return batchService.extractAreasByUsernames(usernames);
    }

    @Override
    public BatchResult<String> extractOperations(List<String> usernames) {
        return batchService.extractOperationsByUsernames(usernames);
    }

    @Override
    public List<User> filterUsersByUsernames(List<User> domainList, List<String> usernames) {
        return userService.filterUsersByUsernames(domainList, usernames);
    }

    @Override
    public List<String> extractAreasFromUsers(List<User> domainList) {
        return userService.extractAreasFromUsers(domainList);
    }

    @Override
    public List<String> extractOperationsFromUsers(List<User> domainList) {
        return userService.extractOperationsFromUsers(domainList);
    }

    @Override
    public BatchResult<User> saveUsers(List<User> domainList) {
        return batchService.saveUsers(domainList);
    }

    @Override
    public BatchResult<User> grantPermissionsToName(List<String> usernames, List<Permission> permissions) {
        return batchService.grantPermissionsToName(usernames, permissions);
    }

    @Override
    public BatchResult<User> grantPermissionsToId(List<Long> ids, List<Permission> permissions) {
        return batchService.grantPermissionsToId(ids, permissions);
    }

    @Override
    public BatchResult<User> revokeAreasByNames(List<String> usernames, List<String> areas) {
        return batchService.revokeAreasByNames(usernames, areas);
    }

    @Override
    public BatchResult<User> revokeOperationsByNames(List<String> usernames, List<String> operations) {
        return batchService.revokeOperationsByNames(usernames, operations);
    }

    @Override
    public BatchResult<User> revokeAreasByIds(List<Long> ids, List<String> areas) {
        return batchService.revokeAreasByIds(ids, areas);
    }

    @Override
    public BatchResult<User> clearPermissionsByNames(List<String> usernames) {
        return batchService.clearPermissions(usernames);
    }

}
