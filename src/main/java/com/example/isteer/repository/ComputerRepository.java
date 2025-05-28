package com.example.isteer.repository;

import com.example.isteer.entity.Computers;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class ComputerRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ComputerRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Computers computer) {
        String sql = "INSERT INTO Computers (uuid, ip_address, hostName, os_name, os_version, location, created_at, updated_at) " +
                     "VALUES (:uuid, :ipAddress, :hostName, :osName, :osVersion, :location, :createdAt, :updatedAt)";
        computer.setUuid(UUID.randomUUID().toString());

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("uuid", computer.getUuid())
                .addValue("ipAddress", computer.getIpAddress())
                .addValue("hostName", computer.getHostName())
                .addValue("osName", computer.getOsName())
                .addValue("osVersion", computer.getOsVersion())
                .addValue("location", computer.getLocation())
                .addValue("createdAt", computer.getCreatedAt())
                .addValue("updatedAt", computer.getUpdatedAt());

        jdbcTemplate.update(sql, params);
        return 1; // Assuming the insert is successful, return 1
    }

    public List<Computers> findAll() {
        String sql = "SELECT id, uuid, ip_address, hostName, os_name, os_version, location, is_active, status, created_at, updated_at FROM Computers WHERE status = 1";
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), this::mapRowToComputer);
    }

    public Computers findByUuid(String uuid) {
        String sql = "SELECT id, uuid, ip_address, hostName, os_name, os_version, location, is_active, status, created_at, updated_at FROM Computers WHERE uuid = :uuid AND status = 1 AND is_active = TRUE";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", uuid);

        try {
            return jdbcTemplate.queryForObject(sql, params, this::mapRowToComputer);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int update(String uuid, Computers computer) {
        String sql = "UPDATE Computers SET ip_address = :ipAddress, hostName = :hostName, os_name = :osName, " +
                     "os_version = :osVersion, location = :location, " +
                     "updated_at = :updatedAt WHERE uuid = :uuid";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("ipAddress", computer.getIpAddress())
                .addValue("hostName", computer.getHostName())
                .addValue("osName", computer.getOsName())
                .addValue("osVersion", computer.getOsVersion())
                .addValue("location", computer.getLocation())
                .addValue("isActive", computer.isActive())
                .addValue("status", computer.getStatus())
                .addValue("updatedAt", computer.getUpdatedAt())
                .addValue("uuid", uuid);

        return jdbcTemplate.update(sql, params);
    }

    public int softDelete(String uuid) {
        String sql = "UPDATE Computers SET status = 0, updated_at = CURRENT_TIMESTAMP WHERE uuid = :uuid AND status = 1";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", uuid);
        return jdbcTemplate.update(sql, params);
    }

    public int deactivate(String uuid) {
        String sql = "UPDATE Computers SET is_active = FALSE, updated_at = CURRENT_TIMESTAMP WHERE uuid = :uuid AND status = 1 AND is_active = TRUE";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", uuid);
        return jdbcTemplate.update(sql, params);
    }

    private Computers mapRowToComputer(ResultSet rs, int rowNum) throws SQLException {
        Computers computer = new Computers();
        computer.setId(rs.getLong("id"));
        computer.setUuid(rs.getString("uuid"));
        computer.setIpAddress(rs.getString("ip_address"));
        computer.setHostName(rs.getString("hostName"));
        computer.setOsName(rs.getString("os_name"));
        computer.setOsVersion(rs.getString("os_version"));
        computer.setLocation(rs.getString("location"));
        computer.setActive(rs.getBoolean("is_active"));
        computer.setStatus(rs.getByte("status"));
        computer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        computer.setUpdatedAt(rs.getTimestamp("updated_at") != null ? 
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return computer;
    }
}
