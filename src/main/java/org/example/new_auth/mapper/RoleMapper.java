package org.example.new_auth.mapper;

import org.example.new_auth.domain.Role;
import org.example.new_auth.dto.request.RoleRequest;
import org.example.new_auth.dto.response.RoleResponse;
import org.example.new_auth.external.request.ExternalRoleRequest;
import org.example.new_auth.external.response.ExternalRoleResponse;
import org.example.new_auth.util.MapperUtils;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    private final PermissionMapper permissionMapper;

    public RoleMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    public Role fromResponse(ExternalRoleResponse response) {
        return new Role(
                response.id(),
                response.code(),
                response.description(),
                response.organization(),
                MapperUtils.mapList(response.permissions(), permissionMapper::fromResponse)
        );
    }

    public Role fromRequest(RoleRequest request) {
        return new Role(
                request.id(),
                request.code(),
                request.description(),
                request.organization(),
                MapperUtils.mapList(request.permissions(), permissionMapper::fromRequest)
        );
    }

    public RoleResponse toResponse(Role role) {
        return new RoleResponse(
                role.id(),
                role.code(),
                role.description(),
                role.organization(),
                MapperUtils.mapList(role.permissions(), permissionMapper::toResponse)
        );
    }

    public ExternalRoleRequest toRequest(Role role) {
        return new ExternalRoleRequest(
                role.id(),
                role.code(),
                role.description(),
                role.organization(),
                MapperUtils.mapList(role.permissions(), permissionMapper::toRequest)
        );
    }

}
