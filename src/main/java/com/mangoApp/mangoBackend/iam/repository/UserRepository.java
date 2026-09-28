package com.mangoApp.mangoBackend.iam.repository;

import com.mangoApp.mangoBackend.iam.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
        rs.getBoolean("is_verified"),
        rs.getString("full_name"),
        rs.getString("phone")
    );

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, tenant_id, email, password_hash, role, is_verified, full_name, phone FROM users WHERE email = ?";
        return jdbcTemplate.query(sql, rowMapper, email).stream().findFirst();
    }

    public Long save(User user) {
        String sql = "INSERT INTO users (tenant_id, email, password_hash, role, full_name, phone) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, user.tenantId());
            ps.setString(2, user.email());
            ps.setString(3, user.passwordHash());
            ps.setString(4, user.role());
            ps.setString(5, user.fullName());
            ps.setString(6, user.phone());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
}
