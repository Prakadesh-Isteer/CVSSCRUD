package com.example.isteer.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;

public class Applications {

	    private String id;
	 
	    private String computerId;
	    private String name;
	    private String version;
	    private String vendor;
	    private LocalDate installDate;
	    @JsonIgnore
	    private String status;
	    public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
	    
		public LocalDateTime getUpdatedAt() {
			return updatedAt;
		}
		public void setUpdatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getComputerId() {
			return computerId;
		}
		public void setComputerId(String computerId) {
			this.computerId = computerId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public String getVendor() {
			return vendor;
		}
		public void setVendor(String vendor) {
			this.vendor = vendor;
		}
		public LocalDate getInstallDate() {
			return installDate;
		}
		public void setInstallDate(LocalDate installDate) {
			this.installDate = installDate;
		}
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
}
	    