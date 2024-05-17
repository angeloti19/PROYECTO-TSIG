package com.tsig.backend.models;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry; 
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.buffer.BufferParameters;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "auto")
public class AutoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "matricula")
    private String matricula;

    @Column(name = "dist_max")
    private double dist_max;

    @Column(name="recorrido", columnDefinition = "Geometry(LineString,32721)")
    private LineString recorrido;

    @Column(name = "electrico")
    private Boolean electrico;

    @Transient
    private List<Coordinate> coord;

    // Método para calcular el buffer
    public Geometry calcularBuffer(LineString recorrido, double dist_max) {

        // Crear un objeto BufferParameters para configurar el estilo de la tapa final del buffer
        BufferParameters bufferParameters = new BufferParameters();
        // Establecer el estilo de la tapa final del buffer (redondeada)
        bufferParameters.setEndCapStyle(BufferParameters.CAP_ROUND);
        
        // Crear un objeto BufferOp con el recorrido y los parámetros del buffer
        BufferOp bufferOp = new BufferOp(recorrido, bufferParameters);
        
        // Obtener y devolver la geometría resultante del buffer con la distancia máxima especificada
        return bufferOp.getResultGeometry(dist_max);
    }
}
