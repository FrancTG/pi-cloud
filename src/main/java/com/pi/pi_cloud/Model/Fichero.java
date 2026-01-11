package com.pi.pi_cloud.Model;

import jakarta.persistence.*;

import javax.crypto.SecretKey;
import java.util.*;

@Entity
public class Fichero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Lob
    private byte[] datos;

    @ManyToMany
    @JoinTable(
            name = "usuario_fichero",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "fichero_id")
    )
    private List<Usuario> usuarios = new ArrayList<Usuario>();

    @Lob
    private HashMap<String, byte[]> clavesCompartidas = new HashMap<>();

    private String resumenDatos;

    public Fichero() {
    }

    public Fichero(String nombre, byte[] datos, List<Usuario> usuarios, HashMap<String, byte[]> clavesCompartidas, String resumenDatos) {
        this.nombre = nombre;
        this.datos = datos;
        this.usuarios = usuarios;
        this.clavesCompartidas = clavesCompartidas;
        this.resumenDatos = resumenDatos;
    }

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

    public String getResumenDatos() {
        return resumenDatos;
    }

    public void setResumenDatos(String resumenDatos) {
        this.resumenDatos = resumenDatos;
    }
}