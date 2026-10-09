package org.example.new_auth.mapper;

import org.example.new_auth.domain.User;
import org.example.new_auth.domain.UserIdProductIds;
import org.example.new_auth.domain.UsernameUserId;
import org.example.new_auth.dto.request.UserIdProductIdsRequest;
import org.example.new_auth.dto.request.UserRequest;
import org.example.new_auth.dto.response.UserIdUsernamesResponse;
import org.example.new_auth.dto.response.UserResponse;
import org.example.new_auth.dto.response.UsernameUserIdResponse;
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

    public User toDomain(ExternalUserResponse userResponse) {
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
                this.mapList(userResponse.permissions(), permissionMapper::toDomain),
                this.mapList(userResponse.roles(), roleMapper::toDomain)
        );
    }

    public User toDomain(UserRequest userRequest) {
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
                this.mapList(userRequest.permissions(), permissionMapper::toDomain),
                this.mapList(userRequest.roles(), roleMapper::toDomain)
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

    public UserIdUsernamesResponse toUserIdUsernames(UserRequest userRequest) {
        return new UserIdUsernamesResponse(userRequest.id(), userRequest.usernames());
    }

    public UsernameUserIdResponse toUsernameUserId(UsernameUserId usernameUserId) {
        return new UsernameUserIdResponse(usernameUserId.username(), usernameUserId.userId());
    }

    public UserIdProductIds toUserIdProductIds(UserIdProductIdsRequest userIdProductIdsRequest) {
        return new UserIdProductIds(userIdProductIdsRequest.userId(), userIdProductIdsRequest.productIds());
    }

    public List<User> toDomainList(List<UserRequest> userRequests) {
        return this.mapList(userRequests, this::toDomain);
    }

    public List<UserResponse> toDtoList(List<User> users) {
        return this.mapList(users, this::toDto);
    }

    public List<UserIdUsernamesResponse> toUserIdUseramesList(List<UserRequest> userRequests) {
        return this.mapList(userRequests, this::toUserIdUsernames);
    }

    public List<UsernameUserIdResponse> toUsernameUserIdList(List<UsernameUserId> usernameUserIdList) {
        return this.mapList(usernameUserIdList, this::toUsernameUserId);
    }

    public List<UserIdProductIds> toUserIdProductIdsList(List<UserIdProductIdsRequest> userIdProductIdsRequestList) {
        return this.mapList(userIdProductIdsRequestList, this::toUserIdProductIds);
    }

}
