package org.example.new_auth.mapper;

import org.example.new_auth.domain.MultiUsernameUser;
import org.example.new_auth.dto.response.MultiUsernameUserResponse;
import org.example.new_auth.util.MapperUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MultiUsernameUserMapper {

    public MultiUsernameUserResponse toResponse(MultiUsernameUser multiUsernameUser) {
        return new MultiUsernameUserResponse(multiUsernameUser.matchedUsername(), multiUsernameUser.userId(), multiUsernameUser.usernames());
    }

    public List<MultiUsernameUserResponse> toResponseList(List<MultiUsernameUser> multiUsernameUserList) {
        return MapperUtils.mapList(multiUsernameUserList, this::toResponse);
    }

}
