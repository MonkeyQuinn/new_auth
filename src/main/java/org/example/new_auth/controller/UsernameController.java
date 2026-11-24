package org.example.new_auth.controller;

import org.example.new_auth.batch.BatchResult;
import org.example.new_auth.dto.request.UsernamesAreasRequest;
import org.example.new_auth.dto.request.UsernamesOperationsRequest;
import org.example.new_auth.service.auth.AuthService;
import org.example.new_auth.util.BatchResponsesUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.USERNAMES)
public class UsernameController {

    private final AuthService authService;

    public UsernameController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/filter/by-areas")
    public ResponseEntity<BatchResult<String>> filterUsernamesByAreas(@RequestBody UsernamesAreasRequest body) {
        BatchResult<String> response = authService.filterUsernamesByAreas(body.usernames(), body.areas());
        return BatchResponsesUtils.toResponse(response);
    }

    @PostMapping("/filter/by-operations")
    public ResponseEntity<BatchResult<String>> filterUsernamesByOperations(@RequestBody UsernamesOperationsRequest body) {
        BatchResult<String> response = authService.filterUsernamesByOperations(body.usernames(), body.operations());
        return BatchResponsesUtils.toResponse(response);
    }

}
