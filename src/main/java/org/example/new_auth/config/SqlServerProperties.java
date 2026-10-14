package org.example.new_auth.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "sqlserver")
@Validated
public record SqlServerProperties(@NotBlank(message = "SqlServer host cannot blank") String host,
                                  @Min(value = 1, message = "Port must be positive")
                                  @Max(value = 65535, message = "Port must be valid")
                                  int port,
                                  @NotBlank(message = "SqlServer database name cannot blank") String database,
                                  @NotBlank(message = "SqlServer username cannot blank") String username,
                                  @NotBlank(message = "SqlServer password cannot blank") String password,
                                  boolean encrypt,
                                  boolean trustServerCertificate) {
}
