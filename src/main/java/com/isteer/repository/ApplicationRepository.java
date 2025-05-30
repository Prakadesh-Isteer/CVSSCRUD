package com.isteer.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.isteer.entity.Application;
import com.isteer.util.ApplicationRowMapper;

@Repository
public class ApplicationRepository {
	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;
	@Transactional
	public int save(Application application) {
		String sql = "INSERT INTO applications (uuid, computer_uuid, name, version, vendor, install_date, created_at) "
				+ "VALUES (:uuid, :computerId, :name, :version, :vendor, :installDate,  :createdAt)";
		application.setUuid(UUID.randomUUID().toString());

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", application.getUuid())
				.addValue("computerId", application.getComputerUuid()).addValue("name", application.getName())
				.addValue("version", application.getVersion()).addValue("vendor", application.getVendor())
				.addValue("installDate", application.getInstalledDate())
				.addValue("createdAt", application.getCreatedAt());

		jdbcTemplate.update(sql, params);
		return 1; // Assuming the insert is successful, return 1
	}
	@Transactional
	public List<Application> findAll(String uuid) {
		String sql;
		MapSqlParameterSource params = new MapSqlParameterSource();

		if (uuid == null) {
			sql = "SELECT id, uuid, computer_uuid, name, version, vendor, install_date,status, created_at, updated_at FROM applications WHERE status = true";
		} else {
			sql = "SELECT id, uuid, computer_uuid, name, version, vendor, install_date,status, created_at, updated_at FROM applications "
					+ "WHERE computer_uuid = :computerId AND status = true";
			params.addValue("computerId", uuid);
		}

		return jdbcTemplate.query(sql, params, new ApplicationRowMapper());
	}
	@Transactional
	public Application findByUuid(String uuid) {
		String sql = "SELECT id, uuid, computer_uuid, name, version, vendor, install_date,status, created_at, updated_at FROM applications"
				+ " WHERE uuid = :uuid AND status = true";
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", uuid);

		try {
			return jdbcTemplate.queryForObject(sql, params,new ApplicationRowMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	@Transactional
	public int update(String uuid, Application application) {
		String sql = "UPDATE applications SET name = :name, version = :version, "
				+ "vendor = :vendor, install_date = :installDate, updated_at = :updatedAt " + "WHERE uuid = :uuid";

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("name", application.getName())
				.addValue("version", application.getVersion()).addValue("vendor", application.getVendor())
				.addValue("installDate", application.getInstalledDate()).addValue("updatedAt", application.getUpdatedAt())
				.addValue("uuid", uuid);

		return jdbcTemplate.update(sql, params);
	}
	@Transactional
	public int softDelete(String uuid) {
		String sql = "UPDATE applications SET status = 0, updated_at = CURRENT_TIMESTAMP WHERE uuid = :uuid AND status = true";
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", uuid);

		return jdbcTemplate.update(sql, params);
	}

}
