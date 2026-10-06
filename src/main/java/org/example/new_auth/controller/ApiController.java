package org.example.new_auth.controller;

import org.example.new_auth.dto.request.*;
import org.example.new_auth.mapper.PermissionMapper;
import org.example.new_auth.mapper.UserMapper;
import org.example.new_auth.domain.User;
import org.example.new_auth.dto.response.UserIdNamesResponse;
import org.example.new_auth.dto.response.UserResponse;
import org.example.new_auth.batch.BatchResult;
import org.example.new_auth.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api/auth")
public class ApiController {

    private final AuthService authService;
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;

    public ApiController(AuthService authService, UserMapper userMapper, PermissionMapper permissionMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
    }

    @GetMapping(value = "/users", params = "username")
    public ResponseEntity<UserResponse> getUserByUsername(@RequestParam String username) {
        User user = authService.getUserByUsername(username);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping(value = "/users", params = "id")
    public ResponseEntity<UserResponse> getUserById(@RequestParam Long id) {
        User user = authService.getUserById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping("/users/by-usernames")
    public ResponseEntity<BatchResult<UserResponse>> getUsersByUsernames(@RequestBody UsernamesRequest body) {
        BatchResult<User> users = authService.getUsersByUsernames(body.usernames());
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/users/by-user-ids")
    public ResponseEntity<BatchResult<UserResponse>> getUsersByIds(@RequestBody UserIdsRequest body) {
        BatchResult<User> users = authService.getUsersByIds(body.userIds());
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/user-ids/by-usernames")
    public ResponseEntity<BatchResult<Long>> getUserIdsByUsernames(@RequestBody UsernamesRequest body) {
        BatchResult<Long> userIds = authService.getUserIdsByUsernames(body.usernames());
        return ResponseEntity.ok(userIds);
    }

    @PostMapping("/areas/by-usernames")
    public ResponseEntity<BatchResult<String>> getAreasByUsernames(@RequestBody UsernamesRequest body) {
        BatchResult<String> areas = authService.getAreasByUsernames(body.usernames());
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/areas/by-user-ids")
    public ResponseEntity<BatchResult<String>> getAreasByUserIds(@RequestBody UserIdsRequest body) {
        BatchResult<String> areas = authService.getAreasByUserIds(body.userIds());
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/operations/by-usernames")
    public ResponseEntity<BatchResult<String>> getOperationsByUsernames(@RequestBody UsernamesRequest body) {
        BatchResult<String> operations = authService.getOperationsByUsernames(body.usernames());
        return ResponseEntity.ok(operations);
    }

    @PostMapping("/operations/by-user-ids")
    public ResponseEntity<BatchResult<String>> getOperationsByUserIds(@RequestBody UserIdsRequest body) {
        BatchResult<String> operations = authService.getOperationsByUserIds(body.userIds());
        return ResponseEntity.ok(operations);
    }

    @PostMapping("/users/filter/by-usernames")
    public ResponseEntity<List<UserResponse>> filterUsersByUsernames(@RequestBody UsersUsernamesRequest body) {
        List<User> users = authService.filterUsersByUsernames(userMapper.toDomainList(body.users()), body.usernames());
        return ResponseEntity.ok(userMapper.toDtoList(users));
    }

    @PostMapping("/usernames/filter/by-areas")
    public ResponseEntity<BatchResult<String>> filterUsernamesByAreas(@RequestBody UsernamesAreasRequest body) {
        BatchResult<String> usernames = authService.filterUsernamesByAreas(body.usernames(), body.areas());
        return ResponseEntity.ok(usernames);
    }

    @PostMapping("/usernames/filter/by-operations")
    public ResponseEntity<BatchResult<String>> filterUsernamesByOperations(@RequestBody UsernamesOperationsRequest body) {
        BatchResult<String> usernames = authService.filterUsernamesByOperations(body.usernames(), body.operations());
        return ResponseEntity.ok(usernames);
    }

    @PostMapping("/user-ids/filter/by-areas")
    public ResponseEntity<BatchResult<Long>> filterUserIdsByAreas(@RequestBody UserIdsAreasRequest body) {
        BatchResult<Long> userIds = authService.filterUserIdsByAreas(body.userIds(), body.areas());
        return ResponseEntity.ok(userIds);
    }

    @PostMapping("/user-ids/filter/by-operations")
    public ResponseEntity<BatchResult<Long>> filterUserIdsByOperations(@RequestBody UserIdsOperationsRequest body) {
        BatchResult<Long> userIds = authService.filterUserIdsByOperations(body.userIds(), body.operations());
        return ResponseEntity.ok(userIds);
    }

    @PostMapping("/user-identities/extract")
    public ResponseEntity<List<UserIdNamesResponse>> extractUserIdNames(@RequestBody UsersRequest body) {
        List<UserIdNamesResponse> userIdNames = userMapper.toUserIdNamesList(body.users());
        return ResponseEntity.ok(userIdNames);
    }

    @PostMapping("/areas/extract")
    public ResponseEntity<List<String>> extractAreas(@RequestBody UsersRequest body) {
        List<String> areas = authService.extractAreas(userMapper.toDomainList(body.users()));
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/operations/extract")
    public ResponseEntity<List<String>> extractOperations(@RequestBody UsersRequest body) {
        List<String> operations = authService.extractOperations(userMapper.toDomainList(body.users()));
        return ResponseEntity.ok(operations);
    }

    @PostMapping("/users/save")
    public ResponseEntity<BatchResult<UserResponse>> saveUsers(@RequestBody UsersRequest body) {
        BatchResult<User> users = authService.saveUsers(userMapper.toDomainList(body.users()));
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/permissions/grant/by-usernames")
    public ResponseEntity<BatchResult<UserResponse>> grantPermissionsByUsernames(@RequestBody UsernamesPermissionsRequest body) {
        BatchResult<User> users = authService.grantPermissionsByUsernames(body.usernames(), permissionMapper.toDomainList(body.permissions()));
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/permissions/grant/by-user-ids")
    public ResponseEntity<BatchResult<UserResponse>> grantPermissionsByUserIds(@RequestBody UserIdsPermissionsRequest body) {
        BatchResult<User> users = authService.grantPermissionsByUserIds(body.userIds(), permissionMapper.toDomainList(body.permissions()));
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/permissions/old-products/grant/by-user-ids")
    public ResponseEntity<BatchResult<UserResponse>> grantOldProductsByUserIds(@RequestBody UserIdProductIdsBatchRequest body, @RequestParam(required = false, defaultValue = "100") int pack, @RequestParam(required = false, defaultValue = "15000") int interval) {
        BatchResult<User> users = authService.grantOldProductsByUserIds(body.userIdsProductIds(), pack, interval);
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/areas/revoke/by-usernames")
    public ResponseEntity<BatchResult<UserResponse>> revokeAreasByUsernames(@RequestBody UsernamesAreasRequest body) {
        BatchResult<User> users = authService.revokeAreasByUsernames(body.usernames(), body.areas());
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/operations/revoke/by-usernames")
    public ResponseEntity<BatchResult<UserResponse>> revokeOperationsByUsernames(@RequestBody UsernamesOperationsRequest body) {
        BatchResult<User> users = authService.revokeOperationsByUsernames(body.usernames(), body.operations());
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/areas/revoke/by-user-ids")
    public ResponseEntity<BatchResult<UserResponse>> revokeAreasByUserIds(@RequestBody UserIdsAreasRequest body) {
        BatchResult<User> users = authService.revokeAreasByUserIds(body.userIds(), body.areas());
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/operations/revoke/by-user-ids")
    public ResponseEntity<BatchResult<UserResponse>> revokeOperationsByUserIds(@RequestBody UserIdsOperationsRequest body) {
        BatchResult<User> users = authService.revokeOperationsByUserIds(body.userIds(), body.operations());
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/permissions/clear/by-usernames")
    public ResponseEntity<BatchResult<UserResponse>> clearPermissionsByUsernames(@RequestBody UsernamesRequest body) {
        BatchResult<User> users = authService.clearPermissionsByUsernames(body.usernames());
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/permissions/clear/by-user-ids")
    public ResponseEntity<BatchResult<UserResponse>> clearPermissionsByUserIds(@RequestBody UserIdsRequest body) {
        BatchResult<User> users = authService.clearPermissionsByUserIds(body.userIds());
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    private <T, R> BatchResult<R> mapBatchResult(BatchResult<T> source, Function<List<T>, List<R>> mapper) {
        return new BatchResult<>(mapper.apply(source.success()), source.errors());
    }

}
