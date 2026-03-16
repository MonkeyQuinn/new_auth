package org.example.new_auth.service.permission;

import org.example.new_auth.domain.Permission;
import org.example.new_auth.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static org.example.new_auth.util.AuthUtils.*;

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
