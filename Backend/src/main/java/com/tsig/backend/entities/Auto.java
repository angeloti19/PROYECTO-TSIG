package com.tsig.backend.entities;

import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "auto")
public class Auto {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matricula", unique = true, nullable = false)
    private String matricula;

    @Column(name = "dist_max")
    private double dist_max;

    @Column(name="recorrido", columnDefinition = "Geometry(LineString,32721)")
    private LineString recorrido;

    @Column(name = "electrico")
    private Boolean electrico;

    @Column(name="ubicacion", columnDefinition = "Geometry(Point,32721)")
    private Point ubicacion;

    @JsonBackReference
    @ManyToOne(/*cascade = CascadeType.ALL, fetch = FetchType.LAZY*/)
    @JoinColumn(name = "automotora_id")
    private Automotora automotora;

    public Auto() {
    }

    public Auto(String matricula, double dist_max, LineString recorrido, Boolean electrico, Point ubicacion,
            Automotora automotora) {
        this.matricula = matricula;
        this.dist_max = dist_max;
        this.recorrido = recorrido;
        this.electrico = electrico;
        this.ubicacion = ubicacion;
        this.automotora = automotora;
    }

}
