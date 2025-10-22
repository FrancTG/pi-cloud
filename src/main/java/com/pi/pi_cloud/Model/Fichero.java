package com.pi.pi_cloud.Model;

import jakarta.persistence.*;

import javax.crypto.SecretKey;

@Entity
public class Fichero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Lob
    private byte[] datos;

    private SecretKey claveCifrada;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Fichero() {

    }

    public Fichero(String nombre, byte[] datos, SecretKey claveCifrada, Usuario usuario) {
        this.nombre = nombre;
        this.datos = datos;
        this.claveCifrada = claveCifrada;
        this.usuario = usuario;
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

    public SecretKey getClaveCifrada() {
        return claveCifrada;
    }

    public void setClaveCifrada(SecretKey claveCifrada) {
        this.claveCifrada = claveCifrada;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
