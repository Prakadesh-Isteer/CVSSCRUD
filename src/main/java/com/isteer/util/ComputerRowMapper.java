package com.isteer.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.isteer.entity.Computer;

public class ComputerRowMapper implements RowMapper<Computer> {

	@Override
	public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
		Computer computer = new Computer();
		computer.setId(rs.getLong("id"));
		computer.setUuid(rs.getString("uuid"));
		computer.setIpAddress(rs.getString("ip_address"));
		computer.setHostName(rs.getString("hostName"));
		computer.setOsName(rs.getString("os_name"));
		computer.setOsVersion(rs.getString("os_version"));
		computer.setLocation(rs.getString("location"));
		computer.setActive(rs.getBoolean("is_active"));
		computer.setStatus(rs.getBoolean("status"));
		computer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
		computer.setUpdatedAt(
				rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null);
		return computer;
	}

}
