package org.example.new_auth.mapper;

import org.example.new_auth.model.domain.Permission;
import org.example.new_auth.model.dto.request.PermissionRequest;
import org.example.new_auth.model.dto.response.PermissionResponse;
import org.example.new_auth.model.external.request.ExternalPermissionRequest;
import org.example.new_auth.model.external.response.ExternalPermissionResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionMapper extends BaseMapper {

    public Permission toDomain(ExternalPermissionResponse response) {
        return new Permission(
                response.area(),
                response.topic(),
                response.operation()
        );
    }

    public Permission toDomain(PermissionRequest request) {
        return new Permission(
                request.area(),
                request.topic(),
                request.operation()
        );
    }

    public Permission toDomain(ExternalPermissionRequest request) {
        return new Permission(
                request.area(),
                request.topic(),
                request.operation()
        );
    }

    public PermissionResponse toDto(Permission permission) {
        return new PermissionResponse(
                permission.area(),
                permission.topic(),
                permission.operation()
        );
    }

    public ExternalPermissionRequest toExternal(Permission permission) {
        return new ExternalPermissionRequest(
                permission.area(),
                permission.topic(),
                permission.operation()
        );
    }

    public List<Permission> toDomainList(List<PermissionRequest> permissions) {
        return this.mapList(permissions, this::toDomain);
    }

}
