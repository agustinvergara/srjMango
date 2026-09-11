package com.mangoApp.mangoBackend.iam.repository;

import com.mangoApp.mangoBackend.iam.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> rowMapper = (rs, rowNum) -> new User(
        rs.getLong("id"),
        rs.getLong("tenant_id"),
        rs.getString("email"),
        rs.getString("password_hash"),
        rs.getString("role"),
        rs.getBoolean("is_verified")
    );

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, tenant_id, email, password_hash, role, is_verified FROM users WHERE email = ?";
        return jdbcTemplate.query(sql, rowMapper, email).stream().findFirst();
    }
}