package com.tsig.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tsig.backend.entities.Sucursal;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    
}
