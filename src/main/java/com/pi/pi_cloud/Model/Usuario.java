package com.pi.pi_cloud.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String email;
    private String password;
    private boolean isAdmin;

    @NotNull
    private String salt;

    @Column(name = "totp_secret")
    private String totpSecret; // Clave secreta para Google Authenticator

    @Column(length = 4096)
    private String publicKey;  // Clave pública RSA (codificada Base64)

    @Column(length = 4096)
    private String encryptedPrivateKey; // Clave privada cifrada con PBKDF2

    @ManyToMany(mappedBy = "usuarios")
    Set<Fichero> ficheros = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;


    public Usuario() {
    }

    public Usuario(String email, String password, boolean isAdmin, String salt, Set<Fichero> ficheros, Departamento departamento) {
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.salt = salt;
        this.ficheros = ficheros;
        this.departamento = departamento;
    }

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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getTotpSecret() {
        return totpSecret;
    }

    public void setTotpSecret(String totpSecret) {
        this.totpSecret = totpSecret;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }

    public void setEncryptedPrivateKey(String encryptedPrivateKey) {
        this.encryptedPrivateKey = encryptedPrivateKey;
    }

    public Set<Fichero> getFicheros() {
        return ficheros;
    }

    public void setFicheros(Set<Fichero> ficheros) {
        this.ficheros = ficheros;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}
