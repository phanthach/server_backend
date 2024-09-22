package org.example.server.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", nullable = false)
    private int userId;

    @Column( name = "fullname", nullable = false, length = 255)
    private String fullname;

    @Column(name = "birthDay", nullable = false, length = 255)
    private String birthDay;

    @Column(name = "email",nullable = true, length = 255, unique = true)
    private String email;

    @Column(name = "password",nullable = false, length = 255)
    private String password;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "phoneNumber", nullable = false, length = 255)
    private String phoneNumber;

    @Column(name = "roleId", nullable = false)
    private Long roleId;

    @Column(name = "createdAt", nullable = false)
    private String createdAt;

    @Column(name = "isBlocked", nullable = false)
    private int isBlocked;

    @Column(name = "licenseNumber", nullable = true, length = 255)
    private String licenseNumber;

    @Column(name = "companyName", nullable = true, length = 255)
    private String companyName;

    // Getters và Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(int isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
