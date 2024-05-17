package com.tsig.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsig.backend.models.AutoModel;

public interface AutoRepository extends JpaRepository<AutoModel, Long> {

    AutoModel findByMatricula(String matricula);
    
}
