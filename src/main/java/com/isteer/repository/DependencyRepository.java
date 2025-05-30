package com.isteer.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.isteer.entity.Dependency;
import com.isteer.util.DependencyRowMapper;

@Repository
public class DependencyRepository {
	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;
    
	@Transactional
	public int save(Dependency dependency) {
		String sql = "INSERT INTO dependencies (uuid, application_uuid, name, version, group_id, artifact_id, created_at) "
				+ "VALUES (:uuid, :applicationId, :name, :version, :groupId, :artifactId, :createdAt)";

		dependency.setUuid(UUID.randomUUID().toString());

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", dependency.getUuid())
				.addValue("applicationId", dependency.getApplicationUuid()).addValue("name", dependency.getName())
				.addValue("version", dependency.getVersion()).addValue("groupId", dependency.getGroupId())
				.addValue("artifactId", dependency.getArtifactId()).addValue("createdAt", dependency.getCreatedAt());

		return jdbcTemplate.update(sql, params);

	}

	@Transactional
	public List<Dependency> findAll(String applicationId) {
		String sql;
		MapSqlParameterSource params = new MapSqlParameterSource();

		if (applicationId == null) {
			sql = "SELECT id, uuid, application_uuid, name, version, group_id, artifact_id, status, created_at, updated_at "
					+ "FROM dependencies WHERE status = TRUE";
		} else {
			sql = "SELECT id, uuid, application_uuid, name, version, group_id, artifact_id, status, created_at, updated_at "
					+ "FROM dependencies WHERE application_uuid = :applicationId AND status = TRUE";
			params.addValue("applicationId", applicationId);
		}

		return jdbcTemplate.query(sql, params, new DependencyRowMapper());
	}
	@Transactional
	public Dependency findByUuid(String uuid) {
		String sql = "SELECT id, uuid, application_uuid, name, version, group_id, artifact_id, status, created_at, updated_at "
				+ "FROM dependencies WHERE uuid = :uuid AND status = TRUE";

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", uuid);

		try {
			return jdbcTemplate.queryForObject(sql, params, new DependencyRowMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	@Transactional
	public int update(String uuid, Dependency dependency) {
		String sql = "UPDATE dependencies SET name = :name, version = :version, "
				+ "group_id = :groupId, artifact_id = :artifactId, updated_at = :updatedAt WHERE uuid = :uuid";

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("name", dependency.getName())
				.addValue("version", dependency.getVersion()).addValue("groupId", dependency.getGroupId())
				.addValue("artifactId", dependency.getArtifactId()).addValue("updatedAt", dependency.getUpdatedAt())
				.addValue("uuid", uuid);

		return jdbcTemplate.update(sql, params);
	}
	@Transactional
	public int softDelete(String uuid) {
		String sql = "UPDATE dependencies SET status = 0, updated_at = CURRENT_TIMESTAMP WHERE uuid = :uuid AND status = TRUE";

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("uuid", uuid);

		return jdbcTemplate.update(sql, params);
	}

}
