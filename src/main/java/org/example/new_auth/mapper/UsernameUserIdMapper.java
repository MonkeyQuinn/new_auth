package org.example.new_auth.mapper;

import org.example.new_auth.domain.UsernameUserId;
import org.example.new_auth.dto.request.UsernameUserIdRequest;
import org.example.new_auth.dto.response.UsernameUserIdResponse;
import org.example.new_auth.util.MapperUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsernameUserIdMapper {

    public UsernameUserId fromRequest(UsernameUserIdRequest usernameUserIdRequest) {
        return new UsernameUserId(usernameUserIdRequest.username(), usernameUserIdRequest.userId());
    }

    public List<UsernameUserId> fromRequestList(List<UsernameUserIdRequest> usernameUserIdRequestList) {
        return MapperUtils.mapList(usernameUserIdRequestList, this::fromRequest);
    }

    public UsernameUserIdResponse toResponse(UsernameUserId usernameUserId) {
        return new UsernameUserIdResponse(usernameUserId.username(), usernameUserId.userId());
    }

    public List<UsernameUserIdResponse> toResponseList(List<UsernameUserId> usernameUserIdList) {
        return MapperUtils.mapList(usernameUserIdList, this::toResponse);
    }

}
