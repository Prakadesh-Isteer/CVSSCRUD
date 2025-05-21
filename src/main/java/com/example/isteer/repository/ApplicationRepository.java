package com.example.isteer.repository;

import com.example.isteer.entity.Applications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ApplicationRepository {

	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;  // ✅ NamedParameterJdbcTemplate retained

    public Applications save(Applications application) {
        String sql = "INSERT INTO applications (id, computer_id, name, version, vendor, install_date, created_at) " +
                     "VALUES (:id, :computerId, :name, :version, :vendor, :installDate, :createdAt)";

        application.setId(UUID.randomUUID().toString());

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", application.getId())
                .addValue("computerId", application.getComputerId())
                .addValue("name", application.getName())
                .addValue("version", application.getVersion())
                .addValue("vendor", application.getVendor())
                .addValue("installDate", application.getInstallDate())
                .addValue("createdAt", application.getCreatedAt());

        jdbcTemplate.update(sql, params);
        return application;
    }

    public List<Applications> findAll() {
        String sql = "SELECT id, computer_id, name, version, vendor, install_date, created_at, updated_at, status FROM applications where status = 'ACTIVE'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToApplication(rs));
    }

    public Applications findById(String id) {
        String sql = "SELECT * FROM applications WHERE id = :id AND status = 'ACTIVE'";
        Map<String, Object> params = Collections.singletonMap("id", id);
        List<Applications> result = jdbcTemplate.query(sql, params, (rs, rowNum) -> mapRowToApplication(rs));
        return result.isEmpty() ? null : result.get(0);
    }

    public int update(String id, Applications application) {
        String sql = "UPDATE applications SET  name = :name, version = :version, " +
                     "vendor = :vendor,updated_at = :updatedAt, install_date = :installDate WHERE id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
               
                .addValue("name", application.getName())
                .addValue("version", application.getVersion())
                .addValue("vendor", application.getVendor())
                .addValue("installDate", application.getInstallDate())
                .addValue("updatedAt", application.getUpdatedAt())
                .addValue("id", id);

        return jdbcTemplate.update(sql, params);
    }

    public int delete(String id) {
        String sql = "UPDATE applications SET status = 'DELETED', updated_at = CURRENT_TIMESTAMP WHERE id = :id" +
					 " AND status = 'ACTIVE'";
        Map<String, Object> params = Collections.singletonMap("id", id);
        return jdbcTemplate.update(sql, params);
    }

    private Applications mapRowToApplication(ResultSet rs) throws SQLException {
        Applications application = new Applications();
        application.setId(rs.getString("id"));
        application.setComputerId(rs.getString("computer_id"));
        application.setName(rs.getString("name"));
        application.setVersion(rs.getString("version"));
        application.setVendor(rs.getString("vendor"));
        application.setInstallDate(rs.getDate("install_date") != null ?
                rs.getDate("install_date").toLocalDate() : null);
        application.setStatus(rs.getString("status"));
        application.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        if (rs.getTimestamp("updated_at") != null) {
			application.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
		}
        return application;
    }
}
