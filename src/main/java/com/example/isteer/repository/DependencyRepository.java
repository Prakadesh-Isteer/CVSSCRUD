package com.example.isteer.repository;

import com.example.isteer.entity.Dependency;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class DependencyRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DependencyRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Dependency dependency) {
        String sql = "INSERT INTO dependencies (uuid, application_uuid, name, version, group_id, artifact_id, created_at) " +
                     "VALUES (:uuid, :applicationId, :name, :version, :groupId, :artifactId, :createdAt)";

        dependency.setUuid(UUID.randomUUID().toString());

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("uuid", dependency.getUuid())
                .addValue("applicationId", dependency.getApplicationUuid())
                .addValue("name", dependency.getName())
                .addValue("version", dependency.getVersion())
                .addValue("groupId", dependency.getGroupId())
                .addValue("artifactId", dependency.getArtifactId())
                .addValue("createdAt", dependency.getCreatedAt());

       return jdbcTemplate.update(sql, params);
       
    }

    public List<Dependency> findAll(String applicationId) {
        String sql;
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (applicationId == null) {
            sql = "SELECT id, uuid, application_uuid, name, version, group_id, artifact_id, status, created_at, updated_at " +
                  "FROM dependencies WHERE status = 1";
        } else {
            sql = "SELECT id, uuid, application_uuid, name, version, group_id, artifact_id, status, created_at, updated_at " +
                  "FROM dependencies WHERE application_uuid = :applicationId AND status = 1";
            params.addValue("applicationId", applicationId);
        }

        return jdbcTemplate.query(sql, params, this::mapRowToDependency);
    }

    public Dependency findByUuid(String uuid) {
        String sql = "SELECT id, uuid, application_uuid, name, version, group_id, artifact_id, status, created_at, updated_at " +
                     "FROM dependencies WHERE uuid = :uuid AND status = 1";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("uuid", uuid);

        try {
            return jdbcTemplate.queryForObject(sql, params, this::mapRowToDependency);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int update(String uuid, Dependency dependency) {
        String sql = "UPDATE dependencies SET name = :name, version = :version, " +
                     "group_id = :groupId, artifact_id = :artifactId, updated_at = :updatedAt " +
                     "WHERE uuid = :uuid";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", dependency.getName())
                .addValue("version", dependency.getVersion())
                .addValue("groupId", dependency.getGroupId())
                .addValue("artifactId", dependency.getArtifactId())
                .addValue("updatedAt", dependency.getUpdatedAt())
                .addValue("uuid", uuid);

        return jdbcTemplate.update(sql, params);
    }

    public int softDelete(String uuid) {
        String sql = "UPDATE dependencies SET status = 0, updated_at = CURRENT_TIMESTAMP WHERE uuid = :uuid";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("uuid", uuid);

        return jdbcTemplate.update(sql, params);
    }

    private Dependency mapRowToDependency(ResultSet rs, int rowNum) throws SQLException {
        Dependency dependency = new Dependency();
        dependency.setId(rs.getLong("id"));
        dependency.setUuid(rs.getString("uuid"));
        dependency.setApplicationUuid(rs.getString("application_uuid"));
        dependency.setName(rs.getString("name"));
        dependency.setVersion(rs.getString("version"));
        dependency.setGroupId(rs.getString("group_id"));
        dependency.setArtifactId(rs.getString("artifact_id"));
        dependency.setStatus(rs.getByte("status"));
        dependency.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        dependency.setUpdatedAt(rs.getTimestamp("updated_at") != null ?
                rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return dependency;
    }
}
