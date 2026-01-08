package com.pi.pi_cloud.dto;


public class UserData {


    private Long id;
    private String email;
    private String password;
    private boolean isAdmin;
    private Long departamentoId;
    private boolean requiresTOTP;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public boolean getRequiresTOTP() {
        return requiresTOTP;
    }

    public void setRequiresTOTP(boolean requiresTOTP) {
        this.requiresTOTP = requiresTOTP;
    }
}
