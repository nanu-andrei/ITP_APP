package com.example.itp_app.POJO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
public class Employee {
    private final StringProperty username;
    private final StringProperty role;

    public final StringProperty firm;

    public Employee(String username, String role, String firm) {
        this.username = new SimpleStringProperty(username);
        this.role = new SimpleStringProperty(role);
        this.firm = new SimpleStringProperty(firm);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public StringProperty roleProperty() {
        return role;
    }

    public String getFirm() {
        return firm.get();
    }

    public StringProperty firmProperty() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm.set(firm);
    }

}
