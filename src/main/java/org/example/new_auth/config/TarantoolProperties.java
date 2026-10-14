package org.example.new_auth.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "tarantool")
@Validated
public record TarantoolProperties(
        @NotBlank(message = "Tarantool host cannot blank") String host,
        @Min(value = 1, message = "Port must be positive")
        @Max(value = 65535, message = "Port must be valid")
        int port,
        @NotBlank(message = "Tarantool username cannot be blank") String username,
        @NotBlank(message = "Tarantool password cannot be blank") String password,
        int connectTimeoutMs
) {

    public TarantoolProperties {
        if (connectTimeoutMs <= 0) {
            connectTimeoutMs = 5000;
        }
    }

}
