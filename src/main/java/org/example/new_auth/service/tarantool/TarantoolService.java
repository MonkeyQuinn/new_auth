package org.example.new_auth.service.tarantool;

import java.util.Optional;

public interface TarantoolService {

    Optional<Long> getUserIdByUsername(String username);

    Optional<String> getSecretByUserId(Long userId);

    boolean updateSecretByUserId(Long userId, String secret);

    boolean deleteUsernameByUserId(Long userId, String username);

}
