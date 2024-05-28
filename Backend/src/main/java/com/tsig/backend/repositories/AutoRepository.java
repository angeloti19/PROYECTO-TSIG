package com.tsig.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tsig.backend.entities.Auto;

@Repository
public interface AutoRepository extends JpaRepository<Auto, Long> {

    Auto findByMatricula(String matricula);

    @Query(value = "SELECT st_astext(geom) FROM \"00departamento\" WHERE nombre = 'MONTEVIDEO'", nativeQuery = true)
    String geoTextoMontevideo();

}
