package com.pi.pi_cloud.dto;

import javax.crypto.SecretKey;

public class FicheroData {

    private Long id;
    private String nombre;
    private byte[] datos;
    private SecretKey claveCifrada;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getDatos() {
        return datos;
    }

    public void setDatos(byte[] datos) {
        this.datos = datos;
    }

    public SecretKey getClaveCifrada() {
        return claveCifrada;
    }

    public void setClaveCifrada(SecretKey claveCifrada) {
        this.claveCifrada = claveCifrada;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
