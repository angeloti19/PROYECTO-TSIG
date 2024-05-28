package com.tsig.backend.entities;

import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sucursal")
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @JsonBackReference
    @ManyToOne(/*cascade = CascadeType.ALL, fetch = FetchType.LAZY*/)
    @JoinColumn(name = "automotora_id")
    private Automotora automotora;

    @Column(name="ubicacion", columnDefinition = "Geometry(Point,32721)")
    private Point ubicacion;

    public Sucursal(String nombre, Automotora automotora, Point ubicacion) {
        this.nombre = nombre;
        this.automotora = automotora;
        this.ubicacion = ubicacion;
    }

    public Sucursal() {
    }

    

}
