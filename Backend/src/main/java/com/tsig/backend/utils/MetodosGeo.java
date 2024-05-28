package com.tsig.backend.utils;


import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.buffer.BufferParameters;

import com.tsig.backend.datatypes.DtAuto;
import com.tsig.backend.datatypes.DtCoordenada;
import com.tsig.backend.datatypes.DtSucursal;

public class MetodosGeo {

    GeometryFactory Factory = new GeometryFactory(new PrecisionModel(), 32721);

    public Point crearUbicacionSucursal(DtSucursal dtSucursal){
        Coordinate coordenadas = new Coordinate(dtSucursal.getCoordenadas().getX(), dtSucursal.getCoordenadas().getY());
        Point ubicacionSucursal = Factory.createPoint(coordenadas);
        return ubicacionSucursal;
    }

    public LineString crearRecorridoAuto(DtAuto dtAuto){
        Coordinate[] coordinates = dtAuto.getRecorrido().stream() //Convierto la lista de DtCoordenadas en un arreglo de Coordinate para crear la LINEA
                                                        .map(coord -> new Coordinate(coord.getX(), coord.getY()))
                                                        .toArray(Coordinate[]::new);
        LineString recorridoAuto = Factory.createLineString(coordinates);
        return recorridoAuto;
    }

    public Point crearUbicacionAuto(DtAuto dtAuto){
        DtCoordenada primerCoordenada = dtAuto.getRecorrido().get(0); //Obtengo la primer coordenada del recorrido
        Coordinate coordinate = new Coordinate(primerCoordenada.getX(), primerCoordenada.getY()); // Creo las coordenadas para el pto de ubicacion del auto
        Point ubicacionAuto = Factory.createPoint(coordinate); // Se crea pto de ubicacion del auto
        return ubicacionAuto;
    }

    public Geometry calcularBuffer(LineString recorrido, Double dist_max ){

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
