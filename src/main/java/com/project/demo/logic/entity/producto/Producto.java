package com.project.demo.logic.entity.producto;

import com.project.demo.logic.entity.categoria.Categoria;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Table (name = "producto")
@Entity

public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private BigDecimal precio;

    @Column(name= "cantidad_stock", nullable = false)
    private Integer cantidadStock;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public BigDecimal getPrecio() {return precio;}
    public void setPrecio(BigDecimal precio) {this.precio = precio;}
    public Integer getCantidadStock() {return cantidadStock;}
    public void setCantidadStock(Integer cantidadStock) {this.cantidadStock = cantidadStock;}
    public Categoria getCategoria() {return categoria;}
    public void setCategoria(Categoria categoria) {this.categoria = categoria;}
}
