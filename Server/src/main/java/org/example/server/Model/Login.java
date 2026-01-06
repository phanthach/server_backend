package org.example.server.Model;

public class Login {
    private String phoneNumber;
    private String password;

    public Login(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }
}
