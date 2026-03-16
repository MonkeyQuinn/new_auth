package org.example.new_auth.service.permission;

import org.example.new_auth.domain.Permission;
import org.example.new_auth.domain.User;

import java.util.List;

public interface PermissionService {

    User grantPermissions(User user, List<Permission> permissions);

    User revokeAreas(User user, List<String> areas);

    User revokeOperations(User user, List<String> operations);

    User clearPermissions(User user);

}
