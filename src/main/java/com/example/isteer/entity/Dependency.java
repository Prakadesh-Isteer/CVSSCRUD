package com.example.isteer.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Dependency {
	  private String id;
	    private String applicationId;
	    private String name;
	    private String version;
	    private String groupId;
	    private String artifactId;
	    private LocalDateTime createdAt;
	    @JsonIgnore
	    private String status;
		public LocalDateTime getUpdatedAt() {
			return updatedAt;
		}
		public void setUpdatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
		}
		private LocalDateTime updatedAt;
	    
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getApplicationId() {
			return applicationId;
		}
		public void setApplicationId(String applicationId) {
			this.applicationId = applicationId;
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
		public String getGroupId() {
			return groupId;
		}
		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}
		public String getArtifactId() {
			return artifactId;
		}
		public void setArtifactId(String artifactId) {
			this.artifactId = artifactId;
		}
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
}
