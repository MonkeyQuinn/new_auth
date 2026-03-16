package org.example.new_auth.mapper;

import org.example.new_auth.domain.User;
import org.example.new_auth.dto.request.UserRequest;
import org.example.new_auth.dto.response.UserIdNamesResponse;
import org.example.new_auth.dto.response.UserResponse;
import org.example.new_auth.external.request.ExternalUserRequest;
import org.example.new_auth.external.response.ExternalUserResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper extends BaseMapper {

    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    public UserMapper(RoleMapper roleMapper, PermissionMapper permissionMapper) {
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    public User toDomain(ExternalUserResponse response) {
        return new User(
                response.id(),
                response.clientId(),
                response.status(),
                response.passChangeNeeded(),
                response.twoFactorEnabled(),
                response.inheritClient(),
                response.firstname(),
                response.lastname(),
                response.refreshTokenTtl(),
                response.accessTokenTtl(),
                response.tokenCount(),
                response.usernames(),
                this.mapList(response.permissions(), permissionMapper::toDomain),
                this.mapList(response.roles(), roleMapper::toDomain)
        );
    }

    public User toDomain(UserRequest request) {
        return new User(
                request.id(),
                request.clientId(),
                request.status(),
                request.passChangeNeeded(),
                request.twoFactorEnabled(),
                request.inheritClient(),
                request.firstname(),
                request.lastname(),
                request.refreshTokenTtl(),
                request.accessTokenTtl(),
                request.tokenCount(),
                request.usernames(),
                this.mapList(request.permissions(), permissionMapper::toDomain),
                this.mapList(request.roles(), roleMapper::toDomain)
        );
    }

    public UserResponse toDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getClientId(),
                user.getStatus(),
                user.getPassChangeNeeded(),
                user.getTwoFactorEnabled(),
                user.getInheritClient(),
                user.getFirstname(),
                user.getLastname(),
                user.getRefreshTokenTtl(),
                user.getAccessTokenTtl(),
                user.getTokenCount(),
                user.getUsernames(),
                this.mapList(user.getPermissions(), permissionMapper::toDto),
                this.mapList(user.getRoles(), roleMapper::toDto)
        );
    }

    public ExternalUserRequest toExternal(User user) {
        return new ExternalUserRequest(
                user.getId(),
                user.getClientId(),
                user.getStatus(),
                user.getPassChangeNeeded(),
                user.getTwoFactorEnabled(),
                user.getInheritClient(),
                user.getFirstname(),
                user.getLastname(),
                user.getRefreshTokenTtl(),
                user.getAccessTokenTtl(),
                user.getTokenCount(),
                user.getUsernames(),
                this.mapList(user.getPermissions(), permissionMapper::toExternal),
                this.mapList(user.getRoles(), roleMapper::toExternal)
        );
    }

    public UserIdNamesResponse toUserIdNames(UserRequest body) {
        return new UserIdNamesResponse(body.id(), body.usernames());
    }

    public List<User> toDomainList(List<UserRequest> userRequests) {
        return this.mapList(userRequests, this::toDomain);
    }

    public List<UserResponse> toDtoList(List<User> users) {
        return this.mapList(users, this::toDto);
    }

    public List<UserIdNamesResponse> toUserIdNamesList(List<UserRequest> userRequests) {
        return this.mapList(userRequests, this::toUserIdNames);
    }

}
