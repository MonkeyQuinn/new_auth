package org.example.new_auth.controller;

import org.example.new_auth.batch.BatchResult;
import org.example.new_auth.domain.MultiUsernameUser;
import org.example.new_auth.dto.request.UsernamesRequest;
import org.example.new_auth.dto.request.UsersRequest;
import org.example.new_auth.dto.response.MultiUsernameUserResponse;
import org.example.new_auth.dto.response.UserIdUsernamesResponse;
import org.example.new_auth.mapper.MultiUsernameUserMapper;
import org.example.new_auth.mapper.UserIdUsernamesMapper;
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
@RequestMapping(ApiPaths.USER_IDENTITIES)
public class UserIdentitiesController {

    private final AuthService authService;
    private final UserIdUsernamesMapper userIdUsernamesMapper;
    private final MultiUsernameUserMapper multiUsernameUserMapper;

    public UserIdentitiesController(AuthService authService, UserIdUsernamesMapper userIdUsernamesMapper, MultiUsernameUserMapper multiUsernameUserMapper) {
        this.authService = authService;
        this.userIdUsernamesMapper = userIdUsernamesMapper;
        this.multiUsernameUserMapper = multiUsernameUserMapper;
    }

    @PostMapping("/extract")
    public ResponseEntity<List<UserIdUsernamesResponse>> extractUserIdNames(@RequestBody UsersRequest body) {
        List<UserIdUsernamesResponse> response = userIdUsernamesMapper.toResponseFromRequestList(body.users());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/group/by-usernames")
    public ResponseEntity<BatchResult<MultiUsernameUserResponse>> groupUserIdentitiesByUsernames(@RequestBody UsernamesRequest body) {
        BatchResult<MultiUsernameUser> batch = authService.groupUserIdentitiesByUsernames(body.usernames());
        BatchResult<MultiUsernameUserResponse> response = MapperUtils.mapBatchResult(batch, multiUsernameUserMapper::toResponseList);
        return BatchResponsesUtils.toResponse(response);
    }

}
