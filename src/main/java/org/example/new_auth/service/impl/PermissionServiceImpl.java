package org.example.new_auth.service.impl;

import org.example.new_auth.model.domain.Permission;
import org.example.new_auth.model.domain.User;
import org.example.new_auth.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static org.example.new_auth.util.UserUtils.*;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Override
    public User grantPermissions(User user, List<Permission> permissions) {
        List<Permission> safePermissions = nonNullStream(permissions).toList();
        return modify(user, u -> u.grantPermissions(safePermissions));
    }

    @Override
    public User clearPermissions(User user) {
        return modify(user, User::clearPermissions);
    }

    @Override
    public User revokeAreas(User user, List<String> areas) {
        List<String> safeAreas = ofNullableStream(areas).toList();
        return modify(user, u -> u.revokePermissionsByAreas(safeAreas));
    }

    @Override
    public User revokeOperations(User user, List<String> operations) {
        List<String> safeOperations = ofNullableStream(operations).toList();
        return modify(user, u -> u.revokePermissionsByOperations(safeOperations));
    }

    private User modify(User user, Consumer<User> modifier) {
        Objects.requireNonNull(user, "User must not be null");
        modifier.accept(user);
        return user;
    }

}
