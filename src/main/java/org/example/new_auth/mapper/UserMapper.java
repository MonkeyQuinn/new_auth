package org.example.new_auth.mapper;

import org.example.new_auth.domain.User;
import org.example.new_auth.dto.request.UserRequest;
import org.example.new_auth.dto.response.UserResponse;
import org.example.new_auth.external.request.ExternalUserRequest;
import org.example.new_auth.external.response.ExternalUserResponse;
import org.example.new_auth.util.MapperUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    public UserMapper(RoleMapper roleMapper, PermissionMapper permissionMapper) {
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    public User fromResponse(ExternalUserResponse userResponse) {
        return new User(
                userResponse.id(),
                userResponse.clientId(),
                userResponse.status(),
                userResponse.passChangeNeeded(),
                userResponse.twoFactorEnabled(),
                userResponse.inheritClient(),
                userResponse.firstname(),
                userResponse.lastname(),
                userResponse.refreshTokenTtl(),
                userResponse.accessTokenTtl(),
                userResponse.tokenCount(),
                userResponse.usernames(),
                MapperUtils.mapList(userResponse.permissions(), permissionMapper::fromResponse),
                MapperUtils.mapList(userResponse.roles(), roleMapper::fromResponse),
                userResponse.password()
        );
    }

    public User fromRequest(UserRequest userRequest) {
        return new User(
                userRequest.id(),
                userRequest.clientId(),
                userRequest.status(),
                userRequest.passChangeNeeded(),
                userRequest.twoFactorEnabled(),
                userRequest.inheritClient(),
                userRequest.firstname(),
                userRequest.lastname(),
                userRequest.refreshTokenTtl(),
                userRequest.accessTokenTtl(),
                userRequest.tokenCount(),
                userRequest.usernames(),
                MapperUtils.mapList(userRequest.permissions(), permissionMapper::fromRequest),
                MapperUtils.mapList(userRequest.roles(), roleMapper::fromRequest),
                userRequest.password()
        );
    }

    public UserResponse toResponse(User user) {
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
                MapperUtils.mapList(user.getPermissions(), permissionMapper::toResponse),
                MapperUtils.mapList(user.getRoles(), roleMapper::toResponse),
                user.getPassword()
        );
    }

    public ExternalUserRequest toRequest(User user) {
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
                MapperUtils.mapList(user.getPermissions(), permissionMapper::toRequest),
                MapperUtils.mapList(user.getRoles(), roleMapper::toRequest),
                user.getPassword()
        );
    }

    public List<User> fromRequestList(List<UserRequest> userRequests) {
        return MapperUtils.mapList(userRequests, this::fromRequest);
    }

    public List<UserResponse> toResponseList(List<User> users) {
        return MapperUtils.mapList(users, this::toResponse);
    }

}
