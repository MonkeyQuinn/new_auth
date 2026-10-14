package org.example.new_auth.service.tarantool;

import io.tarantool.driver.api.TarantoolClient;
import io.tarantool.driver.api.TarantoolResult;
import io.tarantool.driver.api.conditions.Conditions;
import io.tarantool.driver.api.tuple.TarantoolTuple;
import org.example.new_auth.config.TarantoolErrorHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TarantoolServiceImpl implements TarantoolService {

    private static final String SPACE_USERNAMES = "auth_usernames";
    private static final String SPACE_USERS = "auth_users";

    private final TarantoolClient<TarantoolTuple, TarantoolResult<TarantoolTuple>> client;
    private final TarantoolErrorHandler errorHandler;

    public TarantoolServiceImpl(TarantoolClient<TarantoolTuple, TarantoolResult<TarantoolTuple>> client, TarantoolErrorHandler errorHandler) {
        this.client = client;
        this.errorHandler = errorHandler;
    }

    @Override
    public Optional<Long> getUserIdByUsername(String username) {
        return errorHandler.execute("getUserIdByUsername", SPACE_USERNAMES, () -> {
            TarantoolResult<TarantoolTuple> result = client.space("auth_usernames")
                    .select(Conditions.equals("name", username))
                    .join();

            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0).getLong("user"));
        });
    }

    @Override
    public Optional<String> getSecretByUserId(Long userId) {
        return Optional.empty();
    }

    @Override
    public boolean updateSecretByUserId(Long userId, String secret) {
        return false;
    }

    @Override
    public boolean deleteUsernameByUserId(Long userId, String username) {
        return false;
    }

}
