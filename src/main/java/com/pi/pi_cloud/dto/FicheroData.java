package com.pi.pi_cloud.dto;

import com.pi.pi_cloud.Model.Usuario;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.List;

public class FicheroData {

    private Long id;
    private String nombre;
    private byte[] datos;
    private List<Usuario> usuarios;
    private HashMap<String, byte[]> clavesCompartidas;

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

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public HashMap<String, byte[]> getClavesCompartidas() {
        return clavesCompartidas;
    }

    public void setClavesCompartidas(HashMap<String, byte[]> clavesCompartidas) {
        this.clavesCompartidas = clavesCompartidas;
    }
}
