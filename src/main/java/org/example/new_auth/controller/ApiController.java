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

    @GetMapping("/users/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        User user = authService.getUserByUsername(username);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping("/users:batch-by-usernames")
    public ResponseEntity<BatchResult<UserResponse>> batchUsersByUsernames(@RequestBody UsernamesRequest body) {
        BatchResult<User> users = authService.findUsersByUsernames(body.usernames());
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/users:batch-by-ids")
    public ResponseEntity<BatchResult<UserResponse>> batchUsersByIds(@RequestBody UserIdsRequest body) {
        BatchResult<User> users = authService.findUsersByIds(body.ids());
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/usernames:filter-by-areas")
    public ResponseEntity<BatchResult<String>> filterUsernamesByAreas(@RequestBody UsernamesAreasRequest body) {
        BatchResult<String> areas = authService.filterUsernamesByAreas(body.usernames(), body.areas());
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/user-ids:filter-by-areas")
    public ResponseEntity<BatchResult<Long>> filterUserIdsByAreas(@RequestBody UserIdsAreasRequest body) {
        BatchResult<Long> areas = authService.filterUserIdsByAreas(body.ids(), body.areas());
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/user-ids:filter-by-operations")
    public ResponseEntity<BatchResult<Long>> filterUserIdsByOperations(@RequestBody UserIdsOperationsRequest body) {
        BatchResult<Long> areas = authService.filterUserIdsByOperations(body.ids(), body.operations());
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/areas:batch-extract")
    public ResponseEntity<BatchResult<String>> batchExtractAreas(@RequestBody UsernamesRequest body) {
        BatchResult<String> areas = authService.extractAreas(body.usernames());
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/operations:batch-extract")
    public ResponseEntity<BatchResult<String>> batchExtractOperations(@RequestBody UsernamesRequest body) {
        BatchResult<String> operations = authService.extractOperations(body.usernames());
        return ResponseEntity.ok(operations);
    }

    @PostMapping("/users/filter")
    public ResponseEntity<List<UserResponse>> filterUsers(@RequestBody UsersUsernamesRequest body) {
        List<User> users = authService.filterUsersByUsernames(userMapper.toDomainList(body.users()), body.usernames());
        return ResponseEntity.ok(userMapper.toDtoList(users));
    }

    @PostMapping("/usernames/extract")
    public ResponseEntity<List<UserIdNamesResponse>> extractUserIds(@RequestBody UsersRequest body) {
        List<UserIdNamesResponse> userIdNames = userMapper.toUserIdNamesList(body.users());
        return ResponseEntity.ok(userIdNames);
    }

    @PostMapping("/areas/extract")
    public ResponseEntity<List<String>> extractAreas(@RequestBody UsersRequest body) {
        List<String> areas = authService.extractAreasFromUsers(userMapper.toDomainList(body.users()));
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/operations/extract")
    public ResponseEntity<List<String>> extractOperations(@RequestBody UsersRequest body) {
        List<String> operations = authService.extractOperationsFromUsers(userMapper.toDomainList(body.users()));
        return ResponseEntity.ok(operations);
    }

    @PostMapping("/users:batch-save")
    public ResponseEntity<BatchResult<UserResponse>> batchSaveUsers(@RequestBody UsersRequest body) {
        BatchResult<User> users = authService.saveUsers(userMapper.toDomainList(body.users()));
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/users:grant-permissions")
    public ResponseEntity<BatchResult<UserResponse>> grantPermissionsToUsers(@RequestBody UsernamesPermissionsRequest body) {
        BatchResult<User> users = authService.grantPermissions(body.usernames(), permissionMapper.toDomainList(body.permissions()));
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/users:revoke-areas")
    public ResponseEntity<BatchResult<UserResponse>> revokeAreasFromUsers(@RequestBody UsernamesAreasRequest body) {
        BatchResult<User> users = authService.revokeAreas(body.usernames(), body.areas());
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/users:revoke-operations")
    public ResponseEntity<BatchResult<UserResponse>> revokeOperationsFromUsers(@RequestBody UsernamesOperationsRequest body) {
        BatchResult<User> users = authService.revokeOperations(body.usernames(), body.operations());
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/users:clear-permissions")
    public ResponseEntity<BatchResult<UserResponse>> clearPermissions(@RequestBody UsernamesRequest body) {
        BatchResult<User> users = authService.clearPermissions(body.usernames());
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    private <T, R> BatchResult<R> mapBatchResult(BatchResult<T> source, Function<List<T>, List<R>> mapper) {
        return new BatchResult<>(mapper.apply(source.getSuccess()), source.getErrors());
    }

}
