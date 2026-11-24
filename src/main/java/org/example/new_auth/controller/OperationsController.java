package org.example.new_auth.controller;

import org.example.new_auth.batch.BatchResult;
import org.example.new_auth.domain.User;
import org.example.new_auth.dto.request.*;
import org.example.new_auth.dto.response.UserResponse;
import org.example.new_auth.mapper.UserMapper;
import org.example.new_auth.service.auth.AuthService;
import org.example.new_auth.util.MapperUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.OPERATIONS)
public class OperationsController {

    private final AuthService authService;
    private final UserMapper userMapper;

    public OperationsController(AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @PostMapping("/by-usernames")
    public ResponseEntity<BatchResult<String>> getOperationsByUsernames(@RequestBody UsernamesRequest body) {
        BatchResult<String> response = authService.getOperationsByUsernames(body.usernames());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/by-user-ids")
    public ResponseEntity<BatchResult<String>> getOperationsByUserIds(@RequestBody UserIdsRequest body) {
        BatchResult<String> response = authService.getOperationsByUserIds(body.userIds());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/extract")
    public ResponseEntity<List<String>> extractOperations(@RequestBody UsersRequest body) {
        List<String> response = authService.extractOperations(userMapper.fromRequestList(body.users()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/revoke/by-usernames")
    public ResponseEntity<BatchResult<UserResponse>> revokeOperationsByUsernames(@RequestBody UsernamesOperationsRequest body) {
        BatchResult<User> users = authService.revokeOperationsByUsernames(body.usernames(), body.operations());
        BatchResult<UserResponse> response = MapperUtils.mapBatchResult(users, userMapper::toResponseList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/revoke/by-user-ids")
    public ResponseEntity<BatchResult<UserResponse>> revokeOperationsByUserIds(@RequestBody UserIdsOperationsRequest body) {
        BatchResult<User> users = authService.revokeOperationsByUserIds(body.userIds(), body.operations());
        BatchResult<UserResponse> response = MapperUtils.mapBatchResult(users, userMapper::toResponseList);
        return ResponseEntity.ok(response);
    }

}
