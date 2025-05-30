package com.isteer.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.isteer.entity.Dependency;

public class DependencyRowMapper implements RowMapper<Dependency> {

	@Override
	public Dependency mapRow(ResultSet rs, int rowNum) throws SQLException {
		Dependency dependency = new Dependency();
		dependency.setId(rs.getLong("id"));
		dependency.setUuid(rs.getString("uuid"));
		dependency.setApplicationUuid(rs.getString("application_uuid"));
		dependency.setName(rs.getString("name"));
		dependency.setVersion(rs.getString("version"));
		dependency.setGroupId(rs.getString("group_id"));
		dependency.setArtifactId(rs.getString("artifact_id"));
		dependency.setStatus(rs.getBoolean("status"));
		dependency.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
		dependency.setUpdatedAt(
				rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
		return dependency;
	}

}
