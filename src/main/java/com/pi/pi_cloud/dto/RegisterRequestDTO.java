package com.pi.pi_cloud.dto;

import com.pi.pi_cloud.Model.Departamento;

public class RegisterRequestDTO {
    private String email;
    private String password;
    private boolean isAdmin;
    private Departamento departamentoId;

    public RegisterRequestDTO(String email, String password, boolean isAdmin, Departamento departamentoId) {
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.departamentoId = departamentoId;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }

    public Departamento getDepartamentoId() { return departamentoId; }
    public void setDepartamentoId(Departamento departamentoId) { this.departamentoId = departamentoId; }
}
