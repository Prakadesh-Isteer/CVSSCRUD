package com.example.isteer.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Dependency {
    private Long id;
    private String uuid;
    private String applicationUuid;
    private String name;
    private String version;
    private String groupId;
    private String artifactId;
    private byte status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Vulnerability> vulnerabilities; // For hierarchical JSON

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    public String getArtifactId() { return artifactId; }
    public void setArtifactId(String artifactId) { this.artifactId = artifactId; }
    public byte getStatus() { return status; }
    public void setStatus(byte status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<Vulnerability> getVulnerabilities() { return vulnerabilities; }
    public void setVulnerabilities(List<Vulnerability> vulnerabilities) { this.vulnerabilities = vulnerabilities; }
	public String getApplicationUuid() {
		return applicationUuid;
	}
	public void setApplicationUuid(String applicationUuid) {
		this.applicationUuid = applicationUuid;
	}
}