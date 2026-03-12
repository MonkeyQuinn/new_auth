package org.example.new_auth.service;

import org.example.new_auth.model.domain.Permission;
import org.example.new_auth.model.domain.User;

import java.util.List;

public interface PermissionService {

    User grantPermissions(User user, List<Permission> permissions);

    User revokeAreas(User user, List<String> areas);

    User revokeOperations(User user, List<String> operations);

    User clearPermissions(User user);

}
