package com.tsig.backend.utils;


import java.util.List;

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
import com.tsig.backend.entities.*;

public class MetodosGeo {

    GeometryFactory Factory = new GeometryFactory(new PrecisionModel(), 32721);

    public Point crearPunto(DtCoordenada dtCoordenada){
        Coordinate coordenadas = new Coordinate(dtCoordenada.getX(), dtCoordenada.getY());
        Point punto = Factory.createPoint(coordenadas);
        return punto;
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

    public Geometry calcularBuffer(LineString recorrido, Float dist_max ){

        // Crear un objeto BufferParameters para configurar el estilo de la tapa final del buffer
        BufferParameters bufferParameters = new BufferParameters();
        // Establecer el estilo de la tapa final del buffer (redondeada)
        bufferParameters.setEndCapStyle(BufferParameters.CAP_ROUND);

        // Crear un objeto BufferOp con el recorrido y los parámetros del buffer
        BufferOp bufferOp = new BufferOp(recorrido, bufferParameters);

        // Obtener y devolver la geometría resultante del buffer con la distancia máxima especificada
        return bufferOp.getResultGeometry(dist_max);
    }

    public boolean estaDentroDeBuffer(Point ubicacion, Geometry buffer) {
        return buffer.contains(ubicacion);
    }
   
    // Validacion: que tenga otra sucursal dentro la zona de cobertura que no sea la que pasamos por parametro
    public boolean tieneOtraSucursalDentroDeBuffer(List<Sucursal> sucursales, Long sucId, Geometry buffer) {
        boolean retorno  = sucursales.stream() 
                                    .filter(sucursal -> !sucursal.getId().equals(sucId)) //Filtro por sucursales diferentes a la que me pasaron por parametro
                                    .anyMatch(sucursal -> estaDentroDeBuffer(sucursal.getUbicacion(), buffer)); //Busco si hace match alguna sucursal utilizando la funcion 'estaDentroDeBuffer' pasandole la ubicacion de la sucursal y buffer. Si hace match devuelve true
        return retorno;
    }

}
