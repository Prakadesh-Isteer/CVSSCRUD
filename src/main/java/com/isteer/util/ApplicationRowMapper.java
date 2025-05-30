package com.isteer.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.isteer.entity.Application;

public class ApplicationRowMapper implements RowMapper<Application> {

	@Override
	public Application mapRow(ResultSet rs, int rowNum) throws SQLException {
		Application application = new Application();
		application.setId(rs.getLong("id"));
		application.setUuid(rs.getString("uuid"));
		application.setComputerUuid(rs.getString("computer_uuid"));
		application.setName(rs.getString("name"));
		application.setVersion(rs.getString("version"));
		application.setVendor(rs.getString("vendor"));
		application
				.setInstalledDate(rs.getDate("install_date") != null ? rs.getDate("install_date").toLocalDate() : null);
		application.setStatus(rs.getBoolean("status"));
		application.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
		application.setUpdatedAt(
				rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
		return application;
	}

}
