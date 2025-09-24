package com.kr.libraryapiassignment.security.audit;

public enum AuditLogAction {
    LOGIN("Logged in"),
    FAILED_LOGIN("Failed login"),
    LOGIN_BLOCKED("Login blocked"),

    LOGOUT("Logged out"),
    FAILED_LOGOUT("Failed logout"),

    REGISTER("Registered"),
    FAILED_REGISTER("Failed to register"),

    CREATED_BOOK("Created book"),
    ;

    private final String name;

    AuditLogAction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
