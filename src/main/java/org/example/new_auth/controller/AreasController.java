package org.example.new_auth.controller;

import org.example.new_auth.batch.BatchResult;
import org.example.new_auth.domain.User;
import org.example.new_auth.dto.request.*;
import org.example.new_auth.dto.response.UserResponse;
import org.example.new_auth.mapper.UserMapper;
import org.example.new_auth.service.auth.AuthService;
import org.example.new_auth.util.BatchResponsesUtils;
import org.example.new_auth.util.MapperUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.AREAS)
public class AreasController {

    private final AuthService authService;
    private final UserMapper userMapper;


    public AreasController(AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    // поиск area по списку из username
    @PostMapping("/by-usernames")
    public ResponseEntity<BatchResult<String>> getAreasByUsernames(@RequestBody UsernamesRequest body) {
        BatchResult<String> response = authService.getAreasByUsernames(body.usernames());
        return BatchResponsesUtils.toResponse(response);
    }

    @PostMapping("/by-user-ids")
    public ResponseEntity<BatchResult<String>> getAreasByUserIds(@RequestBody UserIdsRequest body) {
        BatchResult<String> response = authService.getAreasByUserIds(body.userIds());
        return BatchResponsesUtils.toResponse(response);
    }

    @PostMapping("/extract")
    public ResponseEntity<List<String>> extractAreas(@RequestBody UsersRequest body) {
        List<User> users = userMapper.fromRequestList(body.users());
        List<String> response = authService.extractAreas(users);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/revoke/by-usernames")
    public ResponseEntity<BatchResult<UserResponse>> revokeAreasByUsernames(@RequestBody UsernamesAreasRequest body) {
        BatchResult<User> batch = authService.revokeAreasByUsernames(body.usernames(), body.areas());
        BatchResult<UserResponse> response = MapperUtils.mapBatchResult(batch, userMapper::toResponseList);
        return BatchResponsesUtils.toResponse(response);
    }

    @PostMapping("/revoke/by-user-ids")
    public ResponseEntity<BatchResult<UserResponse>> revokeAreasByUserIds(@RequestBody UserIdsAreasRequest body) {
        BatchResult<User> batch = authService.revokeAreasByUserIds(body.userIds(), body.areas());
        BatchResult<UserResponse> response = MapperUtils.mapBatchResult(batch, userMapper::toResponseList);
        return BatchResponsesUtils.toResponse(response);
    }

}
