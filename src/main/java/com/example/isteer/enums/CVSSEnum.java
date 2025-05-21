package com.example.isteer.enums;

public enum CVSSEnum {

    ILLEGAL(7321, "Exactly one of machineId, applicationId, or dependencyId must be set");

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
