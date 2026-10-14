package org.example.new_auth.config;

import io.tarantool.driver.api.TarantoolClient;
import io.tarantool.driver.api.TarantoolClientFactory;
import io.tarantool.driver.api.TarantoolResult;
import io.tarantool.driver.api.tuple.TarantoolTuple;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TarantoolConfig {

    @Bean(destroyMethod = "close")
    public TarantoolClient<TarantoolTuple, TarantoolResult<TarantoolTuple>> tarantoolClient(TarantoolProperties properties) {
        return TarantoolClientFactory.createClient()
                .withAddress(properties.host(), properties.port())
                .withCredentials(properties.username(), properties.password())
                .withConnectTimeout(properties.connectTimeoutMs())
                .build();
    }

}
