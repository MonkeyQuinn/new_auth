package org.example.new_auth.service.tarantool;

import io.tarantool.driver.api.TarantoolClient;
import io.tarantool.driver.api.TarantoolResult;
import io.tarantool.driver.api.conditions.Conditions;
import io.tarantool.driver.api.tuple.TarantoolTuple;
import io.tarantool.driver.api.tuple.operations.TupleOperations;
import org.example.new_auth.config.TarantoolErrorHandler;
import org.springframework.stereotype.Service;

import java.util.Objects;
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
            TarantoolResult<TarantoolTuple> result = client.space(SPACE_USERNAMES)
                    .select(Conditions.equals("name", username))
                    .join();

            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0).getLong("user"));
        });
    }

    @Override
    public Optional<String> getSecretByUserId(Long userId) {
        return errorHandler.execute("getSecretByUserId", SPACE_USERS, () -> {
            TarantoolResult<TarantoolTuple> result = client.space(SPACE_USERS)
                    .select(Conditions.equals("id", userId))
                    .join();

            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0).getString("password"));
        });
    }

    @Override
    public boolean updateSecretByUserId(Long userId, String secret) {
        return errorHandler.execute("updateSecretByUserId", SPACE_USERS, () -> {
            TarantoolResult<TarantoolTuple> found = client.space(SPACE_USERS)
                    .select(Conditions.equals("id", userId))
                    .join();

            if (found.isEmpty()) return false;

            client.space(SPACE_USERS)
                    .update(Conditions.equals("id", userId), TupleOperations.set("password", secret))
                    .join();

            return true;
        });
    }

    @Override
    public boolean deleteUsernameByUserId(Long userId, String username) {
        return errorHandler.execute("deleteUsernameByUserId", SPACE_USERNAMES, () -> {
            TarantoolResult<TarantoolTuple> found = client.space(SPACE_USERNAMES).
                    select(Conditions.equals("name", username))
                    .join();

            if (found.isEmpty()) return false;

            Long actualUserId = found.get(0).getLong("user");
            if (!Objects.equals(actualUserId, userId)) return false;

            client.space(SPACE_USERNAMES)
                    .delete(Conditions.equals("name", username))
                    .join();

            return true;
        });
    }

}
