package org.example.new_auth.mapper;

import org.example.new_auth.dto.request.UserRequest;
import org.example.new_auth.dto.response.UserIdUsernamesResponse;
import org.example.new_auth.util.MapperUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserIdUsernamesMapper {

    public UserIdUsernamesResponse toResponseFromRequest(UserRequest userRequest) {
        return new UserIdUsernamesResponse(userRequest.id(), userRequest.usernames());
    }

    public List<UserIdUsernamesResponse> toResponseFromRequestList(List<UserRequest> userRequestList) {
        return MapperUtils.mapList(userRequestList, this::toResponseFromRequest);
    }

}
