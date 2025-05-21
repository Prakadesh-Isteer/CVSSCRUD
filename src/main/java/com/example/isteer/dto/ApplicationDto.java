package com.example.isteer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ApplicationDto {
	    private String name;
	    private String version;
	    private String vendor;
	    private LocalDate installDate;
	    private LocalDateTime createdAt;
	    
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
