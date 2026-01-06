package org.example.server.Model.Response;

public class LoginResponse {
    private String message;
    private int status;
    private int roleId;
    private String token;

    public LoginResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public LoginResponse(String message, int status, int roleId, String token) {
        this.message = message;
        this.status = status;
        this.roleId = roleId;
        this.token = token;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
