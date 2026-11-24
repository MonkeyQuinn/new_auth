package org.example.new_auth.controller;

import org.example.new_auth.batch.BatchResult;
import org.example.new_auth.domain.Permission;
import org.example.new_auth.domain.User;
import org.example.new_auth.domain.UserIdProductIds;
import org.example.new_auth.dto.request.*;
import org.example.new_auth.dto.response.UserResponse;
import org.example.new_auth.mapper.PermissionMapper;
import org.example.new_auth.mapper.UserIdProductIdsMapper;
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
@RequestMapping(ApiPaths.PERMISSIONS)
public class PermissionController {

    private final AuthService authService;
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;
    private final UserIdProductIdsMapper userIdProductIdsMapper;

    public PermissionController(AuthService authService, UserMapper userMapper, PermissionMapper permissionMapper, UserIdProductIdsMapper userIdProductIdsMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
        this.userIdProductIdsMapper = userIdProductIdsMapper;
    }

    @PostMapping("/grant/by-usernames")
    public ResponseEntity<BatchResult<UserResponse>> grantPermissionsByUsernames(@RequestBody UsernamesPermissionsRequest body) {
        List<Permission> permissions = permissionMapper.fromRequestList(body.permissions());
        BatchResult<User> users = authService.grantPermissionsByUsernames(body.usernames(), permissions);
        return respondWithUsers(users);
    }

    @PostMapping("/grant/by-user-ids")
    public ResponseEntity<BatchResult<UserResponse>> grantPermissionsByUserIds(@RequestBody UserIdsPermissionsRequest body) {
        List<Permission> permissions = permissionMapper.fromRequestList(body.permissions());
        BatchResult<User> users = authService.grantPermissionsByUserIds(body.userIds(), permissions);
        return respondWithUsers(users);
    }

    @PostMapping("/old-products/grant/by-user-ids")
    public ResponseEntity<BatchResult<UserResponse>> grantOldProductsByUserIds(@RequestBody UserIdProductIdsBatchRequest body) {
        List<UserIdProductIds> userIdProductIds = userIdProductIdsMapper.fromRequestList(body.userIdsProductIds());
        BatchResult<User> users = authService.grantOldProductsByUserIds(userIdProductIds);
        return respondWithUsers(users);
    }

    @PostMapping("/clear/by-usernames")
    public ResponseEntity<BatchResult<UserResponse>> clearPermissionsByUsernames(@RequestBody UsernamesRequest body) {
        BatchResult<User> users = authService.clearPermissionsByUsernames(body.usernames());
        return respondWithUsers(users);
    }

    @PostMapping("/clear/by-user-ids")
    public ResponseEntity<BatchResult<UserResponse>> clearPermissionsByUserIds(@RequestBody UserIdsRequest body) {
        BatchResult<User> users = authService.clearPermissionsByUserIds(body.userIds());
        return respondWithUsers(users);
    }

    private ResponseEntity<BatchResult<UserResponse>> respondWithUsers(BatchResult<User> users) {
        BatchResult<UserResponse> response = MapperUtils.mapBatchResult(users, userMapper::toResponseList);
        return ResponseEntity.ok(response);
    }

}
