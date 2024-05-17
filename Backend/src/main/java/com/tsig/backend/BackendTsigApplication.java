package com.tsig.backend;

import org.n52.jackson.datatype.jts.JtsModule;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class BackendTsigApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendTsigApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper(GeometryFactory geometryFactory) {
        ObjectMapper mapper = new ObjectMapper();
        // Configurar la GeometryFactory deseada en el ObjectMapper
        JtsModule jtsModule = new JtsModule(geometryFactory);
        mapper.registerModule(jtsModule);
        return mapper;
    }

    @Bean
    public GeometryFactory geometryFactory() {
        // Configurar la GeometryFactory con el SRID deseado
        return new GeometryFactory(new PrecisionModel(), 32721);
    }
}
