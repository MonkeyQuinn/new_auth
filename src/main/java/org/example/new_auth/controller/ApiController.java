package org.example.new_auth.controller;

import org.example.new_auth.mapper.PermissionMapper;
import org.example.new_auth.mapper.UserMapper;
import org.example.new_auth.model.domain.User;
import org.example.new_auth.model.dto.request.*;
import org.example.new_auth.model.dto.response.UserResponse;
import org.example.new_auth.model.dto.response.batch.BatchResult;
import org.example.new_auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<BatchResult<UserResponse>> batchGetUsersByUsernames(@RequestBody UsernamesRequest body) {
        BatchResult<User> users = authService.findUsersByUsernames(body.usernames());
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/users:batch-by-ids")
    public ResponseEntity<BatchResult<UserResponse>> batchGetUsersByIds(@RequestBody UserIdsRequest body) {
        BatchResult<User> users = authService.findUsersByIds(body.ids());
        return ResponseEntity.ok(mapBatchResult(users, userMapper::toDtoList));
    }

    @PostMapping("/users:filter-usernames-by-areas")
    public ResponseEntity<BatchResult<String>> filterUsernamesByAreas(@RequestBody UsernamesAreasRequest body) {
        BatchResult<String> areas = authService.findUsernamesByRequiredAreas(body.usernames(), body.areas());
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/users:filter-ids-by-areas")
    public ResponseEntity<BatchResult<Long>> filterUserIdsByAreas(@RequestBody UserIdsAreasRequest body) {
        BatchResult<Long> areas = authService.findUserIdsByRequiredAreas(body.ids(), body.areas());
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/users:filter-ids-by-operations")
    public ResponseEntity<BatchResult<Long>> filterUserIdsByOperations(@RequestBody UserIdsOperationsRequest body) {
        BatchResult<Long> areas = authService.findUserIdsByRequiredOperations(body.ids(), body.operations());
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/areas:batch-extract")
    public ResponseEntity<BatchResult<String>> batchExtractAreasByUsernames(@RequestBody UsernamesRequest body) {
        BatchResult<String> areas = authService.extractAreasByUsernames(body.usernames());
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/operations:batch-extract")
    public ResponseEntity<BatchResult<String>> batchExtractOperationsByUsernames(@RequestBody UsernamesRequest body) {
        BatchResult<String> operations = authService.extractOperationsByUsernames(body.usernames());
        return ResponseEntity.ok(operations);
    }

    @PostMapping("/users:filter-local")
    public ResponseEntity<List<UserResponse>> filterUsersLocal(@RequestBody UsersUsernamesRequest body) {
        List<User> users = authService.filterUsersByUsernames(userMapper.toDomainList(body.users()), body.usernames());
        return ResponseEntity.ok(userMapper.toDtoList(users));
    }

    @PostMapping("/usernames:extract-local")
    public ResponseEntity<Map<Long, List<String>>> extractUsersLocal(@RequestBody UsersRequest body) {
        Map<Long, List<String>> usernames = new HashMap<>();
        for (UserRequest user : body.users()) {
            usernames.put(user.id(), user.usernames());
        }
        return ResponseEntity.ok(usernames);
    }

    @PostMapping("/areas:extract-local")
    public ResponseEntity<List<String>> extractAreasLocal(@RequestBody UsersRequest body) {
        List<String> areas = authService.extractAreasFromUsers(userMapper.toDomainList(body.users()));
        return ResponseEntity.ok(areas);
    }

    @PostMapping("/operations:extract-local")
    public ResponseEntity<List<String>> extractOperationsLocal(@RequestBody UsersRequest body) {
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

    private <T, R> BatchResult<R> mapBatchResult(BatchResult<T> source, Function<List<T>, List<R>> mapper) {
        return new BatchResult<>(mapper.apply(source.getSuccess()), source.getErrors());
    }

}
