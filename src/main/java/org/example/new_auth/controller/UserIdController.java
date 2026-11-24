package org.example.new_auth.controller;

import org.example.new_auth.batch.BatchResult;
import org.example.new_auth.domain.UsernameUserId;
import org.example.new_auth.dto.request.UserIdsAreasRequest;
import org.example.new_auth.dto.request.UserIdsOperationsRequest;
import org.example.new_auth.dto.request.UsernamesRequest;
import org.example.new_auth.dto.response.UsernameUserIdResponse;
import org.example.new_auth.mapper.UsernameUserIdMapper;
import org.example.new_auth.service.auth.AuthService;
import org.example.new_auth.util.BatchResponsesUtils;
import org.example.new_auth.util.MapperUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.USER_IDS)
public class UserIdController {

    private final AuthService authService;
    private final UsernameUserIdMapper usernameUserIdMapper;

    public UserIdController(AuthService authService, UsernameUserIdMapper usernameUserIdMapper) {
        this.authService = authService;
        this.usernameUserIdMapper = usernameUserIdMapper;
    }

    @PostMapping("/by-usernames")
    public ResponseEntity<BatchResult<Long>> getUserIdsByUsernames(@RequestBody UsernamesRequest body) {
        BatchResult<Long> response = authService.getUserIdsByUsernames(body.usernames());
        return BatchResponsesUtils.toResponse(response);
    }

    @PostMapping("/by-usernames/linked")
    public ResponseEntity<BatchResult<UsernameUserIdResponse>> getUserIdsByUsernamesLinked(@RequestBody UsernamesRequest body) {
        BatchResult<UsernameUserId> batch = authService.getUserIdsByUsernamesLinked(body.usernames());
        BatchResult<UsernameUserIdResponse> response = MapperUtils.mapBatchResult(batch, usernameUserIdMapper::toResponseList);
        return BatchResponsesUtils.toResponse(response);
    }

    @PostMapping("/filter/by-areas")
    public ResponseEntity<BatchResult<Long>> filterUserIdsByAreas(@RequestBody UserIdsAreasRequest body) {
        BatchResult<Long> response = authService.filterUserIdsByAreas(body.userIds(), body.areas());
        return BatchResponsesUtils.toResponse(response);
    }

    @PostMapping("/filter/by-operations")
    public ResponseEntity<BatchResult<Long>> filterUserIdsByOperations(@RequestBody UserIdsOperationsRequest body) {
        BatchResult<Long> response = authService.filterUserIdsByOperations(body.userIds(), body.operations());
        return BatchResponsesUtils.toResponse(response);
    }

}
