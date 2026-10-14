package org.example.new_auth.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserSqlRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserSqlRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int updateOAuthByUsername(String username, Long oAuth) {
        String sql = "update dbo.c_ua_user set oauth_identity_id = ? where username = ?";
        return jdbcTemplate.update(sql, oAuth, username);
    }

}
