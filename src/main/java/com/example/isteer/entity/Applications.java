package com.example.isteer.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Applications {
    private Long id;
    private String uuid;
    private String computerUuid; // Changed to computerUuid for clarity
    private String name;
    private String version;
    private String vendor;
    private LocalDate installDate;
    private byte status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Dependency> dependencies; // For hierarchical JSON

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }
    public LocalDate getInstallDate() { return installDate; }
    public void setInstallDate(LocalDate installDate) { this.installDate = installDate; }
    public byte getStatus() { return status; }
    public void setStatus(byte status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<Dependency> getDependencies() { return dependencies; }
    public void setDependencies(List<Dependency> dependencies) { this.dependencies = dependencies; }
	public String getComputerUuid() {
		return computerUuid;
	}
	public void setComputerUuid(String computerUuid) {
		this.computerUuid = computerUuid;
	}
}