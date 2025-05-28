package com.example.isteer.enums;

public enum CVSSEnum {

	// General Status Codes
    COMPUTER_ADD(2000, "Computer added successfully"),
    COMPUTER_UPDATE(2002, "Computer updated successfully"),
    COMPUTER_DELETE(2003, "Computer deleted successfully"),
    COMPUTER_NOT_FOUND(2004, "Computer not found"),
    COMPUTER_UUID_EMPTY(2007, "Computer UUID cannot be empty"),
    IP_ADDRESS_INVALID(2001, "Invalid IP address provided"),
    COMPUTER_WITH_SAME_IP_EXISTS(2005, "Computer with same IP address already exists"),
    COMPUTER_DEACTIVATED(2006, "Computer deactivated successfully"),
    COMPUTER_ALREADY_DELETED(2008, "Computer is already deleted or deactivated"),
    // Vulnerability Status Codes
    
    Internal_Server_Error(5000, "Internal server error occurred"),
    
    // Application Status Codes
    APPLICATION_ADD(3000, "Application added successfully"),
    APPLICATION_UPDATE(3001, "Application updated successfully"),
    APPLICATION_DELETE(3002, "Application deleted successfully"),
    APPLICATION_NOT_FOUND(3003, "Application not found"),
    APPLICATION_UUID_EMPTY(3004, "Application UUID cannot be empty"),	
    APPLICATION_WITH_SAME_NAME_EXISTS(3005, "Application with same name already exists for this computer"),
    
    // Dependency Status Codes
    DEPENDENCY_ADD(4000, "Dependency added successfully"),
    DEPENDENCY_UPDATE(4001, "Dependency updated successfully"),
    DEPENDENCY_DELETE(4002, "Dependency deleted successfully"),
    DEPENDENCY_NOT_FOUND(4003, "Dependency not found"),
    DEPENDENCY_UUID_EMPTY(4004, "Dependency UUID cannot be empty"),
    DEPENDENCY_WITH_SAME_NAME_EXISTS(4005, "Dependency with same name already exists for this application"),
    
    // Vulnerability Status Codes
    VULNERABILITY_ADD(5000, "Vulnerability added successfully"),
    VULNERABILITY_UPDATE(5001, "Vulnerability updated successfully"),
    VULNERABILITY_DELETE(5002, "Vulnerability deleted successfully"),
    VULNERABILITY_NOT_FOUND(5003, "Vulnerability not found"),
    VULNERABILITY_UUID_EMPTY(5004, "Vulnerability UUID cannot be empty"),
    VULNERABILITY_ALREADY_EXISTS(5005, "Vulnerability already exists for this dependency")
    ;
	

    private final int statusCode;
    private final String statusMessage;

    CVSSEnum(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
