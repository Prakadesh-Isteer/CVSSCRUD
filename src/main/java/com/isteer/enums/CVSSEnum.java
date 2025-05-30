package com.isteer.enums;

public enum CVSSEnum {

	    // Enum constants with only code and message key
	    COMPUTER_ADD(2000, "computer.add"),
	    COMPUTER_UPDATE(2002, "computer.update"),
	    COMPUTER_DELETE(2003, "computer.delete"),
	    COMPUTER_NOT_FOUND(2004, "computer.notfound"),
	    COMPUTER_UUID_EMPTY(2007, "computer.uuid.empty"),
	    IP_ADDRESS_INVALID(2001, "ip.invalid"),
	    COMPUTER_WITH_SAME_IP_EXISTS(2005, "computer.duplicate.ip"),
	    COMPUTER_DEACTIVATED(2006, "computer.deactivated"),
	    COMPUTER_ALREADY_DELETED(2008, "computer.already.deleted"),
	    Internal_Server_Error(9000, "internal.error"),

    
	 // Application
	    APPLICATION_ADD(3000, "application.add"),
	    APPLICATION_UPDATE(3001, "application.update"),
	    APPLICATION_DELETE(3002, "application.delete"),
	    APPLICATION_NOT_FOUND(3003, "application.notfound"),
	    APPLICATION_UUID_EMPTY(3004, "application.uuid.empty"),
	    APPLICATION_WITH_SAME_NAME_EXISTS(3005, "application.name.exists"),

	    // Dependency
	    DEPENDENCY_ADD(4000, "dependency.add"),
	    DEPENDENCY_UPDATE(4001, "dependency.update"),
	    DEPENDENCY_DELETE(4002, "dependency.delete"),
	    DEPENDENCY_NOT_FOUND(4003, "dependency.notfound"),
	    DEPENDENCY_UUID_EMPTY(4004, "dependency.uuid.empty"),
	    DEPENDENCY_WITH_SAME_NAME_EXISTS(4005, "dependency.name.exists"),

	    // Vulnerability
	    VULNERABILITY_ADD(5000, "vulnerability.add"),
	    VULNERABILITY_UPDATE(5001, "vulnerability.update"),
	    VULNERABILITY_DELETE(5002, "vulnerability.delete"),
	    VULNERABILITY_NOT_FOUND(5003, "vulnerability.notfound"),
	    VULNERABILITY_UUID_EMPTY(5004, "vulnerability.uuid.empty"),
	    VULNERABILITY_ALREADY_EXISTS(5005, "vulnerability.exists"),
	    VULNERABILITY_SEVERITY_INVALID(5006, "vulnerability.severity.invalid"),
	    VULNERABILITY_CVSS_SCORE_INVALID(5007, "vulnerability.cvss.score.invalid"),
	    INVALID_INPUT(5008, "invalid.input"),
	    DATA_INTEGRITY_VIOLATION(5009, "data.integrity.violation"),
	    VALIDATION_ERROR(5010, "validation.error"),
	    NULL_POINTER_EXCEPTION(5011, "null.pointer.exception"),
	    INVALID_SQL_SYNTAX(5012, "invalid.sql.syntax"),
	    ILLEGAL_ARGUMENT(5013, "illegal.argument.exception"), COMPUTER_ACTIVATED(5014, "computer.activated");
	     
	
	    private final int statusCode;
	    private final String messageKey;
	    

	    CVSSEnum(int statusCode, String messageKey) {
	        this.statusCode = statusCode;
	        this.messageKey = messageKey;
	    }

	    public int getStatusCode() {
	        return statusCode;
	    }

	    public String getMessageKey() {
	        return messageKey;
	    }
	}

