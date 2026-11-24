package org.example.new_auth.controller;

import org.example.new_auth.batch.BatchResult;
import org.example.new_auth.domain.User;
import org.example.new_auth.dto.request.UserIdsRequest;
import org.example.new_auth.dto.request.UsernamesRequest;
import org.example.new_auth.dto.request.UsersRequest;
import org.example.new_auth.dto.request.UsersUsernamesRequest;
import org.example.new_auth.dto.response.UserResponse;
import org.example.new_auth.mapper.UserMapper;
import org.example.new_auth.service.auth.AuthService;
import org.example.new_auth.util.BatchResponsesUtils;
import org.example.new_auth.util.MapperUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.USERS)
public class UserController {

    private final AuthService authService;
    private final UserMapper userMapper;

    public UserController(AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @GetMapping(value = "/by-username", params = "username") // поиск юзера по username
    public ResponseEntity<UserResponse> getUserByUsername(@RequestParam String username) {
        User user = authService.getUserByUsername(username);
        UserResponse response = userMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/by-username", params = "id") // поиск юзера по auth_id
    public ResponseEntity<UserResponse> getUserById(@RequestParam Long id) {
        User user = authService.getUserById(id);
        UserResponse response = userMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/by-usernames") // поиск юзеров по списку из username
    public ResponseEntity<BatchResult<UserResponse>> getUsersByUsernames(@RequestBody UsernamesRequest body) {
        BatchResult<User> batch = authService.getUsersByUsernames(body.usernames());
        BatchResult<UserResponse> response = MapperUtils.mapBatchResult(batch, userMapper::toResponseList);
        return BatchResponsesUtils.toResponse(response);
    }

    @PostMapping("/by-user-ids") // поиск юзеров по списку из auth_id
    public ResponseEntity<BatchResult<UserResponse>> getUsersByIds(@RequestBody UserIdsRequest body) {
        BatchResult<User> batch = authService.getUsersByIds(body.userIds());
        BatchResult<UserResponse> response = MapperUtils.mapBatchResult(batch, userMapper::toResponseList);
        return BatchResponsesUtils.toResponse(response);
    }

    @PostMapping("/filter/by-usernames")
    public ResponseEntity<List<UserResponse>> filterUsersByUsernames(@RequestBody UsersUsernamesRequest body) {
        List<User> candidates = userMapper.fromRequestList(body.users());
        List<User> filtered = authService.filterUsersByUsernames(candidates, body.usernames());
        List<UserResponse> response = userMapper.toResponseList(filtered);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<BatchResult<UserResponse>> saveUsers(@RequestBody UsersRequest body) {
        List<User> users = userMapper.fromRequestList(body.users());
        BatchResult<User> batch = authService.saveUsers(users);
        BatchResult<UserResponse> response = MapperUtils.mapBatchResult(batch, userMapper::toResponseList);
        return BatchResponsesUtils.toResponse(response);
    }

}
