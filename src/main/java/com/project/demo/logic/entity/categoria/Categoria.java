package com.project.demo.logic.entity.categoria;

import jakarta.persistence.*;
@Table (name = "categoria")
@Entity

public class Categoria {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String nombre;

    @Column (nullable = false)
    private String descripcion;

    public Long getId() {return id;}
    public void setId(Long id) { this.id = id;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
}
