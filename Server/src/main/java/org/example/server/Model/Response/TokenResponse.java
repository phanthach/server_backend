package org.example.server.Model.Response;

public class TokenResponse {
    private int status;
    private String message;
    private String fullName;

    public TokenResponse(int status, String message, String fullName) {
        this.status = status;
        this.message = message;
        this.fullName = fullName;
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
}
