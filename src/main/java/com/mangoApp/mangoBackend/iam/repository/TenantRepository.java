package com.mangoApp.mangoBackend.iam.repository;

import com.mangoApp.mangoBackend.iam.model.Tenant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
        rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
        rs.getString("province"),
        rs.getString("farm_name"),
        rs.getString("crops_description"),
        rs.getBoolean("has_refrigeration")
    );

    public Optional<Tenant> findById(Long id) {
        String sql = "SELECT id, name, business_type, ruc, is_active, created_at, province, farm_name, crops_description, has_refrigeration FROM tenants WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    public Long save(Tenant tenant) {
        String sql = "INSERT INTO tenants (name, business_type, ruc, province, farm_name, crops_description, has_refrigeration) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tenant.name());
            ps.setString(2, tenant.businessType());
            ps.setString(3, tenant.ruc());
            ps.setString(4, tenant.province());
            ps.setString(5, tenant.farmName());
            ps.setString(6, tenant.cropsDescription());
            ps.setObject(7, tenant.hasRefrigeration());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
}
