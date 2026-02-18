package org.example.new_auth.mapper;

import org.example.new_auth.model.domain.Role;
import org.example.new_auth.model.dto.request.RoleRequest;
import org.example.new_auth.model.dto.response.RoleResponse;
import org.example.new_auth.model.external.request.ExternalRoleRequest;
import org.example.new_auth.model.external.response.ExternalRoleResponse;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper extends BaseMapper {

    private final PermissionMapper permissionMapper;

    public RoleMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    public Role toDomain(ExternalRoleResponse response) {
        return new Role(
                response.id(),
                response.code(),
                response.description(),
                response.organization(),
                this.mapList(response.permissions(), permissionMapper::toDomain)
        );
    }

    public Role toDomain(RoleRequest request) {
        return new Role(
                request.id(),
                request.code(),
                request.description(),
                request.organization(),
                this.mapList(request.permissions(), permissionMapper::toDomain)
        );
    }

    public Role toDomain(ExternalRoleRequest request) {
        return new Role(
                request.id(),
                request.code(),
                request.description(),
                request.organization(),
                this.mapList(request.permissions(), permissionMapper::toDomain)
        );
    }

    public RoleResponse toDto(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getCode(),
                role.getDescription(),
                role.getOrganization(),
                this.mapList(role.getPermissions(), permissionMapper::toDto)
        );
    }

    public ExternalRoleRequest toExternal(Role role) {
        return new ExternalRoleRequest(
                role.getId(),
                role.getCode(),
                role.getDescription(),
                role.getOrganization(),
                this.mapList(role.getPermissions(), permissionMapper::toExternal)
        );
    }

}
