package org.example.new_auth.service.auth;

import org.example.new_auth.batch.BatchResult;
import org.example.new_auth.batch.UserItem;
import org.example.new_auth.domain.Permission;
import org.example.new_auth.domain.User;
import org.example.new_auth.domain.UserIdProductIds;
import org.example.new_auth.domain.UsernameUserId;
import org.example.new_auth.dto.request.SandRecreateRequest;
import org.example.new_auth.dto.response.UsernameMatchResponse;
import org.example.new_auth.repository.UserSqlRepository;
import org.example.new_auth.service.batch.BatchService;
import org.example.new_auth.service.tarantool.TarantoolService;
import org.example.new_auth.service.user.UserQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final BatchService batchService;
    private final UserQueryService userService;
    private final TarantoolService tarantoolService;
    private final UserSqlRepository userSqlRepository;

    public AuthServiceImpl(BatchService batchService, UserQueryService userService, TarantoolService tarantoolService, UserSqlRepository userSqlRepository) {
        this.batchService = batchService;
        this.userService = userService;
        this.tarantoolService = tarantoolService;
        this.userSqlRepository = userSqlRepository;
    }

    @Override
    public User getUserByUsername(String name) {
        return userService.getUserByUsername(name);
    }

    @Override
    public User getUserById(Long id) {
        return userService.getUserById(id);
    }

    @Override
    public BatchResult<User> getUsersByUsernames(List<String> usernames) {
        return batchService.getUsersByUsernames(usernames);
    }

    @Override
    public BatchResult<User> getUsersByIds(List<Long> ids) {
        return batchService.getUsersByIds(ids);
    }

    @Override
    public BatchResult<Long> getUserIdsByUsernames(List<String> usernames) {
        return batchService.getUserIdsByUsernames(usernames);
    }

    @Override
    public BatchResult<UsernameUserId> getUserIdsByUsernamesLinked(List<String> usernames) {
        return batchService.getUserIdsByUsernamesLinked(usernames);
    }

    @Override
    public BatchResult<String> getAreasByUsernames(List<String> usernames) {
        return batchService.getAreasByUsernames(usernames);
    }

    @Override
    public BatchResult<String> getAreasByUserIds(List<Long> ids) {
        return batchService.getAreasByUserIds(ids);
    }

    @Override
    public BatchResult<String> getOperationsByUsernames(List<String> usernames) {
        return batchService.getOperationsByUsernames(usernames);
    }

    @Override
    public BatchResult<String> getOperationsByUserIds(List<Long> ids) {
        return batchService.getOperationsByUserIds(ids);
    }

    @Override
    public List<User> filterUsersByUsernames(List<User> users, List<String> usernames) {
        return userService.filterUsersByUsernames(users, usernames);
    }

    @Override
    public BatchResult<String> filterUsernamesByAreas(List<String> usernames, List<String> areas) {
        return batchService.filterUsernamesByAreas(usernames, areas);
    }

    @Override
    public BatchResult<String> filterUsernamesByOperations(List<String> usernames, List<String> operations) {
        return batchService.filterUsernamesByOperations(usernames, operations);
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
    public BatchResult<UsernameMatchResponse> groupUserIdentitiesByUsernames(List<String> usernames) {
        return batchService.groupUserIdentitiesByUsernames(usernames);
    }

    @Override
    public List<String> extractAreas(List<User> users) {
        return userService.extractAreas(users);
    }

    @Override
    public List<String> extractOperations(List<User> users) {
        return userService.extractOperations(users);
    }

    @Override
    public BatchResult<User> saveUsers(List<User> users) {
        return batchService.saveUsers(users);
    }

    @Override
    public BatchResult<User> grantPermissionsByUsernames(List<String> usernames, List<Permission> permissions) {
        return batchService.grantPermissionsByUsernames(usernames, permissions);
    }

    @Override
    public BatchResult<User> grantPermissionsByUserIds(List<Long> ids, List<Permission> permissions) {
        return batchService.grantPermissionsByUserIds(ids, permissions);
    }

    @Override
    public BatchResult<User> grantOldProductsByUserIds(List<UserIdProductIds> userIdsProductIds) {
        return batchService.grantOldProductsByUserIds(userIdsProductIds);
    }

    @Override
    public BatchResult<User> revokeAreasByUsernames(List<String> usernames, List<String> areas) {
        return batchService.revokeAreasByUsernames(usernames, areas);
    }

    @Override
    public BatchResult<User> revokeOperationsByUsernames(List<String> usernames, List<String> operations) {
        return batchService.revokeOperationsByUsernames(usernames, operations);
    }

    @Override
    public BatchResult<User> revokeAreasByUserIds(List<Long> ids, List<String> areas) {
        return batchService.revokeAreasByUserIds(ids, areas);
    }

    @Override
    public BatchResult<User> revokeOperationsByUserIds(List<Long> ids, List<String> operations) {
        return batchService.revokeOperationsByUserIds(ids, operations);
    }

    @Override
    public BatchResult<User> clearPermissionsByUsernames(List<String> usernames) {
        return batchService.clearPermissionsByUsernames(usernames);
    }

    @Override
    public BatchResult<User> clearPermissionsByUserIds(List<Long> ids) {
        return batchService.clearPermissionsByUserIds(ids);
    }

    @Override
    public BatchResult<UsernameUserId> sandNotMatchesByUsernames(List<UsernameUserId> list) {
        return batchService.sandNotMatchesByUsernames(list);
    }

    @Override
    public BatchResult<User> sandUsersRecreate(List<SandRecreateRequest> list) {
        return batchService.sandUsersRecreate(list);
    }

}
