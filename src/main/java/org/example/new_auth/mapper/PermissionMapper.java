package org.example.new_auth.mapper;

import org.example.new_auth.domain.Permission;
import org.example.new_auth.dto.request.PermissionRequest;
import org.example.new_auth.dto.response.PermissionResponse;
import org.example.new_auth.external.request.ExternalPermissionRequest;
import org.example.new_auth.external.response.ExternalPermissionResponse;
import org.example.new_auth.util.MapperUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionMapper {

    public Permission fromResponse(ExternalPermissionResponse response) {
        return new Permission(
                response.area(),
                response.topic(),
                response.operation()
        );
    }

    public Permission fromRequest(PermissionRequest request) {
        return new Permission(
                request.area(),
                request.topic(),
                request.operation()
        );
    }

    public PermissionResponse toResponse(Permission permission) {
        return new PermissionResponse(
                permission.area(),
                permission.topic(),
                permission.operation()
        );
    }

    public ExternalPermissionRequest toRequest(Permission permission) {
        return new ExternalPermissionRequest(
                permission.area(),
                permission.topic(),
                permission.operation()
        );
    }

    public List<Permission> fromRequestList(List<PermissionRequest> permissions) {
        return MapperUtils.mapList(permissions, this::fromRequest);
    }

}
