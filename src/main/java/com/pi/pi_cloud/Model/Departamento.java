package com.pi.pi_cloud.Model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departamento")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String organizacion;

    @OneToMany(mappedBy = "departamento")
    Set<Fichero> ficheros = new HashSet<>();

    @OneToMany(mappedBy = "departamento")
    Set<User> usuarios = new HashSet<>();

    public Departamento() {

    }

    public Departamento(String nombre, String organizacion) {
        this.nombre = nombre;
        this.organizacion = organizacion;
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

    public String getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(String organizacion) {
        this.organizacion = organizacion;
    }

    public Set<Fichero> getFicheros() {
        return ficheros;
    }

    public void setFicheros(Set<Fichero> ficheros) {
        this.ficheros = ficheros;
    }

    public Set<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<User> usuarios) {
        this.usuarios = usuarios;
    }
}
