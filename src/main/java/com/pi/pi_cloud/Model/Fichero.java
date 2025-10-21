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
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

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

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}
