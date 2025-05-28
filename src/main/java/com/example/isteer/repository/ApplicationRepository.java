package com.example.isteer.repository;

import com.example.isteer.entity.Applications;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class ApplicationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ApplicationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Applications application) {
        String sql = "INSERT INTO applications (uuid, computer_uuid, name, version, vendor, install_date, created_at) " +
                     "VALUES (:uuid, :computerId, :name, :version, :vendor, :installDate,  :createdAt)";
        application.setUuid(UUID.randomUUID().toString());

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("uuid", application.getUuid())
                .addValue("computerId", application.getComputerUuid())
                .addValue("name", application.getName())
                .addValue("version", application.getVersion())
                .addValue("vendor", application.getVendor())
                .addValue("installDate", application.getInstallDate())
                .addValue("status", application.getStatus())
                .addValue("createdAt", application.getCreatedAt());

        jdbcTemplate.update(sql, params);
        return  1; // Assuming the insert is successful, return 1
    }

    public List<Applications> findAll(String uuid) {
        String sql;
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (uuid == null) {
            sql = "SELECT id, uuid, computer_uuid, name, version, vendor, install_date,status, created_at, updated_at FROM applications WHERE status = 1";
        } else {
            sql = "SELECT id, uuid, computer_uuid, name, version, vendor, install_date,status, created_at, updated_at FROM applications WHERE computer_uuid = :computerId AND status = 1";
            params.addValue("computerId", uuid);
        }

        return jdbcTemplate.query(sql, params, this::mapRowToApplication);
    }

    public Applications findByUuid(String uuid) {
        String sql = "SELECT id, uuid, computer_uuid, name, version, vendor, install_date,status, created_at, updated_at FROM applications WHERE uuid = :uuid AND status = 1";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", uuid);

        try {
            return jdbcTemplate.queryForObject(sql, params, this::mapRowToApplication);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int update(String uuid, Applications application) {
        String sql = "UPDATE applications SET name = :name, version = :version, " +
                     "vendor = :vendor, install_date = :installDate, updated_at = :updatedAt " +
                     "WHERE uuid = :uuid";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", application.getName())
                .addValue("version", application.getVersion())
                .addValue("vendor", application.getVendor())
                .addValue("installDate", application.getInstallDate())
                .addValue("updatedAt", application.getUpdatedAt())
                .addValue("uuid", uuid);

        return jdbcTemplate.update(sql, params);
    }

    public int softDelete(String uuid) {
        String sql = "UPDATE applications SET status = 0, updated_at = CURRENT_TIMESTAMP WHERE uuid = :uuid";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", uuid);

        return jdbcTemplate.update(sql, params);
    }

    private Applications mapRowToApplication(ResultSet rs, int rowNum) throws SQLException {
        Applications application = new Applications();
        application.setId(rs.getLong("id"));
        application.setUuid(rs.getString("uuid"));
        application.setComputerUuid(rs.getString("computer_uuid"));
        application.setName(rs.getString("name"));
        application.setVersion(rs.getString("version"));
        application.setVendor(rs.getString("vendor"));
        application.setInstallDate(rs.getDate("install_date") != null ? 
                rs.getDate("install_date").toLocalDate() : null);
        application.setStatus(rs.getByte("status"));
        application.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        application.setUpdatedAt(rs.getTimestamp("updated_at") != null ? 
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return application;
    }
}
