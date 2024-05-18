package com.tsig.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsig.backend.entities.Auto;

public interface AutoRepository extends JpaRepository<Auto, Long> {

    Auto findByMatricula(String matricula);
    
}
