package com.mangoApp.mangoBackend.iam.repository;

import com.mangoApp.mangoBackend.iam.model.Tenant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class TenantRepository {

    private final JdbcTemplate jdbcTemplate;

    public TenantRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Tenant> rowMapper = (rs, rowNum) -> new Tenant(
        rs.getLong("id"),
        rs.getString("name"),
        rs.getString("business_type"),
        rs.getString("ruc"),
        rs.getBoolean("is_active"),
        rs.getTimestamp("created_at").toLocalDateTime()
    );

    public Optional<Tenant> findById(Long id) {
        String sql = "SELECT id, name, business_type, ruc, is_active, created_at FROM tenants WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }
}