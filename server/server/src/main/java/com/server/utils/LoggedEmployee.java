package com.server.utils;

public class LoggedEmployee {

    private static LoggedEmployee instance;
    private Long employeeId;

    private LoggedEmployee() {
    }

    public static synchronized LoggedEmployee getInstance() {
        if (instance == null) {
            instance = new LoggedEmployee();
        }
        return instance;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
