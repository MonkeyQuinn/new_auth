package org.example.new_auth.controller;

import org.example.new_auth.batch.BatchResult;
import org.example.new_auth.domain.User;
import org.example.new_auth.domain.UsernameUserId;
import org.example.new_auth.dto.request.SandRecreateRequest;
import org.example.new_auth.dto.request.UsernameUserIdBatchRequest;
import org.example.new_auth.dto.response.UserResponse;
import org.example.new_auth.dto.response.UsernameUserIdResponse;
import org.example.new_auth.mapper.UserMapper;
import org.example.new_auth.mapper.UsernameUserIdMapper;
import org.example.new_auth.service.auth.AuthService;
import org.example.new_auth.util.MapperUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.SAND)
public class SandManController {

    private final AuthService authService;
    private final UserMapper userMapper;
    private final UsernameUserIdMapper usernameUserIdMapper;

    public SandManController(AuthService authService, UserMapper userMapper, UsernameUserIdMapper usernameUserIdMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
        this.usernameUserIdMapper = usernameUserIdMapper;
    }

    @PostMapping("/username-not-matched-id")
    public ResponseEntity<BatchResult<UsernameUserIdResponse>> sandman(@RequestBody UsernameUserIdBatchRequest body) {
        List<UsernameUserId> usernameUserIds = usernameUserIdMapper.fromRequestList(body.usernamesUserIds());
        BatchResult<UsernameUserId> usernameUserIdBatch = authService.sandNotMatchesByUsernames(usernameUserIds);
        BatchResult<UsernameUserIdResponse> response = MapperUtils.mapBatchResult(usernameUserIdBatch, usernameUserIdMapper::toResponseList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/recreate")
    public ResponseEntity<BatchResult<UserResponse>> sandmanUsersRecreate(@RequestBody List<SandRecreateRequest> body) {
        BatchResult<User> users = authService.sandUsersRecreate(body);
        BatchResult<UserResponse> response = MapperUtils.mapBatchResult(users, userMapper::toResponseList);
        return ResponseEntity.ok(response);
    }

}
