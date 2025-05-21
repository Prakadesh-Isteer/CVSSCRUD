package com.example.isteer.repository;

import com.example.isteer.entity.Computers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ComputerRepository {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;  // NamedParameterJdbcTemplate retained

    public Computers save(Computers computer) {
        String sql = "INSERT INTO computers (id, ip_address, hostname, os_name, os_version, location, created_at, is_active, status) " +
                     "VALUES (:id, :ipAddress, :hostname, :osName, :osVersion, :location, :createdAt , :isActive, :status)";
        
        computer.setId(UUID.randomUUID().toString());

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", computer.getId())
                .addValue("ipAddress", computer.getIpAddress())
                .addValue("hostname", computer.getHostname())
                .addValue("osName", computer.getOsName())
                .addValue("osVersion", computer.getOsVersion())
                .addValue("location", computer.getLocation())
                .addValue("createdAt", computer.getCreatedAt())
                .addValue("isActive", true)
                .addValue("status", "ACTIVE");

        jdbcTemplate.update(sql, params);
        return computer;
    }

    public List<Computers> findAll() {
        String sql = "SELECT id, ip_address, hostname, os_name, os_version, location, created_at,updated_at, is_active, status  FROM computers where status = 'ACTIVE'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> maprowTocomputer(rs));
    }

    public Computers findById(String id) {
        String sql = "SELECT * FROM computers WHERE id = :id AND status = 'ACTIVE'";
        Map<String, Object> params = Collections.singletonMap("id", id);
        List<Computers> result = jdbcTemplate.query(sql, params, (rs, rowNum) -> maprowTocomputer(rs));
        return result.isEmpty() ? null : result.get(0);
    }

    public int update(String id, Computers computer) {
        String sql = "UPDATE computers SET ip_address = :ipAddress, hostname = :hostname, os_name = :osName, " +
                     "os_version = :osVersion, location = :location, updated_at = :updatedAt WHERE id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("ipAddress", computer.getIpAddress())
                .addValue("hostname", computer.getHostname())
                .addValue("osName", computer.getOsName())
                .addValue("osVersion", computer.getOsVersion())
                .addValue("location", computer.getLocation())
                .addValue("updatedAt", computer.getUpdatedAt())
                .addValue("id", id);

        return jdbcTemplate.update(sql, params);
    }

    public int delete(String id) {
    	 String sql = "UPDATE Computers SET status = 'DELETED', updated_at = CURRENT_TIMESTAMP WHERE id = :id AND status = 'ACTIVE'";
        Map<String, Object> params = Collections.singletonMap("id", id);
        return jdbcTemplate.update(sql, params);
    }
    
    public int deactivate(String id) {
        String sql = "UPDATE Computers SET is_active = FALSE, updated_at = CURRENT_TIMESTAMP WHERE id = :id AND status = 'ACTIVE'";
        Map<String, Object> params = Collections.singletonMap("id", id);
        return jdbcTemplate.update(sql, params);
    }

    private Computers maprowTocomputer(ResultSet rs) throws SQLException {
        Computers computer = new Computers();
        computer.setId(rs.getString("id"));
        computer.setIpAddress(rs.getString("ip_address"));
        computer.setHostname(rs.getString("hostname"));
        computer.setOsName(rs.getString("os_name"));
        computer.setOsVersion(rs.getString("os_version"));
        computer.setLocation(rs.getString("location"));
        computer.setActive(rs.getBoolean("is_active"));
        computer.setStatus(rs.getString("status"));
        computer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        if (rs.getTimestamp("updated_at") != null) {
			computer.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
		}
        return computer;
    }
}
// This repository class provides methods to interact with the database for CRUD operations on the Computers entity.