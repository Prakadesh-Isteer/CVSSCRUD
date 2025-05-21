package com.example.isteer.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Computers {

	 private String id;
	    private String ipAddress;
	    private String hostname;
	    private String osName;
	    private String osVersion;
		private String location;
		private boolean isActive;
		@JsonIgnore
	    private String status;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
	    
	    
	    public boolean isActive() {
			return isActive;
		}
		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}
		
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
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
		public String getIpAddress() {
			return ipAddress;
		}
		public void setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
		}
		public String getHostname() {
			return hostname;
		}
		public void setHostname(String hostname) {
			this.hostname = hostname;
		}
		public String getOsName() {
			return osName;
		}
		public void setOsName(String osName) {
			this.osName = osName;
		}
		public String getOsVersion() {
			return osVersion;
		}
		public void setOsVersion(String osVersion) {
			this.osVersion = osVersion;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
	

}
