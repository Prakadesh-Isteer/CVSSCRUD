package com.example.isteer.repository;

import com.example.isteer.entity.Dependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class DependencyRepository {

   @Autowired
   NamedParameterJdbcTemplate jdbcTemplate;  // ✅ NamedParameterJdbcTemplate retained

    public int save(Dependency dependency) {
        String sql = "INSERT INTO dependencies (id, application_id, name, version, group_id, artifact_id, created_at) " +
                     "VALUES (:id, :applicationId, :name, :version, :groupId, :artifactId, :createdAt)";

        dependency.setId(UUID.randomUUID().toString());

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", dependency.getId())
                .addValue("applicationId", dependency.getApplicationId())
                .addValue("name", dependency.getName())
                .addValue("version", dependency.getVersion())
                .addValue("groupId", dependency.getGroupId())
                .addValue("artifactId", dependency.getArtifactId())
                .addValue("createdAt", dependency.getCreatedAt());

       return jdbcTemplate.update(sql, params);
 
    }

    public List<Dependency> findAll() {
        String sql = "SELECT id, application_id, name, version, group_id, artifact_id, created_at, updated_at, status FROM dependencies where status = 'ACTIVE'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToDependency(rs));
    }

    public Dependency findById(String id) {
        String sql = "SELECT * FROM dependencies WHERE id = :id";
        Map<String, Object> params = Collections.singletonMap("id", id);
        List<Dependency> result = jdbcTemplate.query(sql, params, (rs, rowNum) -> mapRowToDependency(rs));
        return result.isEmpty() ? null : result.get(0);
    }

    public int update(String id, Dependency dependency) {
        String sql = "UPDATE dependencies SET application_id = :applicationId, name = :name, version = :version, " +
                     "group_id = :groupId, artifact_id = :artifactId, updated_at = :updatedAt WHERE id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("applicationId", dependency.getApplicationId())
                .addValue("name", dependency.getName())
                .addValue("version", dependency.getVersion())
                .addValue("groupId", dependency.getGroupId())
                .addValue("artifactId", dependency.getArtifactId())
                .addValue("id", id)
                 .addValue("updatedAt", dependency.getUpdatedAt());
        return jdbcTemplate.update(sql, params);
    }

    public int delete(String id) {
        String sql = "UPDATE dependencies SET updated_at = CURRENT_TIMESTAMP WHERE id = :id AND status = 'ACTIVE'";
        Map<String, Object> params = Collections.singletonMap("id", id);
        return jdbcTemplate.update(sql, params);
    }

    private Dependency mapRowToDependency(ResultSet rs) throws SQLException {
        Dependency dependency = new Dependency();
        dependency.setId(rs.getString("id"));
        dependency.setApplicationId(rs.getString("application_id"));
        dependency.setName(rs.getString("name"));
        dependency.setVersion(rs.getString("version"));
        dependency.setGroupId(rs.getString("group_id"));
        dependency.setArtifactId(rs.getString("artifact_id"));
        dependency.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        dependency.setUpdatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
       dependency.setStatus(rs.getString("status"));
        return dependency;
    }
}
