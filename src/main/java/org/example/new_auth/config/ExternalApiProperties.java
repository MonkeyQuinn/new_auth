package org.example.new_auth.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "api")
@Validated
public record ExternalApiProperties(@NotBlank(message = "Base URL cannot blank") String baseUrl,
                                    @NotBlank(message = "Username cannot blank") String userName,
                                    @NotBlank(message = "User password cannot blank") String userPass,
                                    @NotBlank(message = "Client name cannot blank") String clientName,
                                    @NotBlank(message = "Client password cannot blank") String clientPass,
                                    @Valid Endpoints endpoints) {

    public record Endpoints(@NotBlank String login,
                            @NotBlank String username,
                            @NotBlank String userId,
                            @NotBlank String save) {
    }

}
