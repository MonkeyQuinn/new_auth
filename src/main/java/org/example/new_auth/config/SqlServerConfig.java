package org.example.new_auth.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SqlServerConfig {

    @Bean
    public DataSource dataSource(SqlServerProperties properties) {
        String url = String.format(
                "jdbc:sqlserver://%s:%d;databaseName=%s;encrypt=%b;trustServerCertificate=%b",
                properties.host(), properties.port(), properties.database(), properties.encrypt(), properties.trustServerCertificate()
        );

        return DataSourceBuilder.create()
                .url(url)
                .username(properties.username())
                .password(properties.password())
                .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                .build();
    }

}
