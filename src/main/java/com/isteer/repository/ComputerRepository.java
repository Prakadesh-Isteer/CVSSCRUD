package com.isteer.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.isteer.entity.Computer;
import com.isteer.util.ComputerRowMapper;
@Repository
public class ComputerRepository {
	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	public int save(Computer computer) {
		String sql = "INSERT INTO Computers (uuid, ip_address, hostName, os_name, os_version, location, created_at) "
				+ "VALUES (:uuid, :ipAddress, :hostName, :osName, :osVersion, :location, :createdAt)";
		computer.setUuid(UUID.randomUUID().toString());

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", computer.getUuid())
				.addValue("ipAddress", computer.getIpAddress()).addValue("hostName", computer.getHostName())
				.addValue("osName", computer.getOsName()).addValue("osVersion", computer.getOsVersion())
				.addValue("location", computer.getLocation()).addValue("createdAt", computer.getCreatedAt());
				
		jdbcTemplate.update(sql, params);
		return 1; // Assuming the insert is successful, return 1
	}

	public List<Computer> findAll() {
		String sql = "SELECT id, uuid, ip_address, hostName, os_name, os_version, location, is_active, status, created_at, updated_at FROM Computers WHERE status = TRUE";
		return jdbcTemplate.query(sql, new MapSqlParameterSource(), new ComputerRowMapper());
	}

	public Computer findByUuid(String uuid) {
		String sql = "SELECT id, uuid, ip_address, hostName, os_name, os_version, location, is_active, status, created_at, updated_at FROM Computers WHERE uuid = :uuid AND status = TRUE AND is_active = TRUE";
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", uuid);

		try {
			return jdbcTemplate.queryForObject(sql, params, new ComputerRowMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public Computer computerByUuid(String uuid) {
		String sql = "SELECT id, uuid, ip_address, hostName, os_name, os_version, location, is_active, status, created_at, updated_at FROM Computers WHERE uuid = :uuid AND status = TRUE";
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", uuid);

		try {
			return jdbcTemplate.queryForObject(sql, params, new ComputerRowMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public int update(String uuid, Computer computer) {
		String sql = "UPDATE Computers SET ip_address = :ipAddress, hostName = :hostName, os_name = :osName, "
				+ "os_version = :osVersion, location = :location, " + "updated_at = :updatedAt WHERE uuid = :uuid";

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("ipAddress", computer.getIpAddress())
				.addValue("hostName", computer.getHostName()).addValue("osName", computer.getOsName())
				.addValue("osVersion", computer.getOsVersion()).addValue("location", computer.getLocation())
				.addValue("isActive", computer.isActive()).addValue("status", computer.isStatus())
				.addValue("updatedAt", computer.getUpdatedAt()).addValue("uuid", uuid);

		return jdbcTemplate.update(sql, params);
	}

	public int softDelete(String uuid) {
		String sql = "UPDATE Computers SET status = 0, updated_at = CURRENT_TIMESTAMP WHERE uuid = :uuid AND status = TRUE";
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", uuid);
		return jdbcTemplate.update(sql, params);
	}

	public int deactivate(String uuid) {
		String sql = "UPDATE Computers SET is_active = FALSE, updated_at = CURRENT_TIMESTAMP WHERE uuid = :uuid AND status = TRUE AND is_active = TRUE";
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", uuid);
		return jdbcTemplate.update(sql, params);
	}
	
	@Transactional
	public int activateComputer(String uuid) {
	    String updateSql = "UPDATE Computers SET is_active = TRUE, updated_at = CURRENT_TIMESTAMP " +
	                       "WHERE uuid = :uuid AND is_active = FALSE AND status = TRUE";
	    MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", uuid);
	    return jdbcTemplate.update(updateSql, params);
	}
	
	
}