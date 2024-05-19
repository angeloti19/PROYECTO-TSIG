package com.tsig.backend.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "automotora")
public class Automotora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @JsonManagedReference
    @OneToMany(mappedBy = "automotora",cascade = CascadeType.ALL, fetch = FetchType.LAZY , orphanRemoval = true)
    private List<Sucursal> sucursales = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "automotora",cascade = CascadeType.ALL, fetch = FetchType.LAZY , orphanRemoval = true)
    private List<Auto> autos = new ArrayList<>();

    public Automotora() {
    }

    public Automotora(String nombre) {
        this.nombre = nombre;
    }

    public void agregarSucursal(Sucursal sucursal){
        sucursales.add(sucursal);
    }

    public void agregarAuto(Auto auto){
        autos.add(auto);
    }

}
