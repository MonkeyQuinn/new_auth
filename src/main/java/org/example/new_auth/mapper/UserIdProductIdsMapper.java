package org.example.new_auth.mapper;

import org.example.new_auth.domain.UserIdProductIds;
import org.example.new_auth.dto.request.UserIdProductIdsRequest;
import org.example.new_auth.util.MapperUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserIdProductIdsMapper {

    public UserIdProductIds fromRequest(UserIdProductIdsRequest userIdProductIdsRequest) {
        return new UserIdProductIds(userIdProductIdsRequest.userId(), userIdProductIdsRequest.productIds());
    }

    public List<UserIdProductIds> fromRequestList(List<UserIdProductIdsRequest> userIdProductIdsRequestList) {
        return MapperUtils.mapList(userIdProductIdsRequestList, this::fromRequest);
    }

}
