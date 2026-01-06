package org.example.server.Model.Response;

public class TokenResponse {
    private int status;
    private String message;
    private String fullName;
    private Integer roleId;

    public TokenResponse(int status, String message, String fullName, Integer roleId) {
        this.status = status;
        this.message = message;
        this.fullName = fullName;
        this.roleId = roleId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
