<script>
import { store } from '../store.js'
import BotonCircular from './BotonCircular.vue';
import logo from './icons/logo.vue';
import poligono from './icons/poligono.vue'
import ubicacion from './icons/ubicacion.vue'
import Map from "ol/Map";

export default{
    name: 'mapa',
    components: {
        BotonCircular,
        poligono,
        ubicacion
    },
    data(){
        return{
            store,
            projectionName: 'EPSG:32721',
            projectionDef: '+proj=utm +zone=21 +south +datum=WGS84 +units=m +no_defs +type=crs',
            projectionExtent: [166021.44, 1116915.04, 833978.56, 10000000.0],
            anchor: [0.517, 150],
            montevideoExtent: [550943, 6132427, 591081, 6161393]
        }
    },
    methods:{
        resolutionChanged(event) {
            this.store.currentResolution = event.target.getResolution();
            this.store.currentZoom = event.target.getZoom();
        },
        centerChanged(event) {
            this.store.currentCenter = event.target.getCenter();
        },
        rotationChanged(event) {
            this.store.currentRotation = event.target.getRotation();
        },
        geolocationChanged(event){
            this.store.currentGeolocation = event.target.getPosition();
            //this.$refs.view.setCenter(event.target.getPosition());
            //this.puntoSolicitud = event.target.getPosition();
        }
    },
    mounted(){
        store.viewReference = this.$refs.view
        store.mapReference = this.$refs.mapref.map
        store.agregarInteraccion("Point")
        if(store.tipoUsuario == "admin"){
            store.modoInteraccion = "normal"
            store.fetchSucursalesMapa()
            store.fetchAutosMapa()
        }else{
            //Aplicar filtro sobre punto de solicitud
            //Esto se deberia hacer cuando cambia el pto de solicitud tambien
            store.modoInteraccion = "punto-solicitud"
            store.fetchSucursalesMapa()
            store.fetchAutosMapa()
        }
        
        
    },
    computed:{
        obtenerColorUsuario(){
            if(this.store.tipoUsuario == "anonimo"){
                return "#26A099"
            }else{
                return "#FF3A69"
            }
        }
    }
}

</script>

<template>
    <ol-map ref="mapref" :loadTilesWhileAnimating="true" :loadTilesWhileInteracting="true" style="height: 100%">
        <ol-projection-register
            :projectionName="projectionName"
            :projectionDef="projectionDef"
            :projectionExtent="projectionExtent"
            
        />

        <!-- Div overlay -->
        <div class="overlay">
            <div class="barra-y-boton">
                <div style="height: 20px;">
                    <v-expansion-panels>
                    <v-expansion-panel
                        title="Cambiar punto de solicitud"
                        style="width: 275px;"
                    >
                    <v-expansion-panel-text>
                        Aqui se cambiaria el punto de solicitud por direccion o cruce
                    </v-expansion-panel-text>
                    </v-expansion-panel>
                </v-expansion-panels>
                </div>

                <BotonCircular v-show="store.modoInteraccion == 'punto-solicitud'" @click="store.usarUbicacion()" class="boton-ubicacion" :color="obtenerColorUsuario"><ubicacion/></BotonCircular>
            </div>

            <BotonCircular v-show="store.modoInteraccion == 'punto-solicitud'" class="boton-poligono" :color="obtenerColorUsuario"><poligono/></BotonCircular>
        </div>

      <ol-view
        ref="view"
        :center="store.center"
        :rotation="store.rotation"
        :zoom="store.zoom"
        :projection="projectionName"
        :extent="montevideoExtent"
        @change:center="centerChanged"
        @change:resolution="resolutionChanged"
        @change:rotation="rotationChanged"
      />
        <!-- Capas del servidor -->
        <!-- <ol-layer-group>
            <ol-tile-layer :zIndex="1001" :visible=true minZoom="13">
                <ol-source-tile-wms
                    url="http://localhost:8080/geoserver/wms"
                    layers="tsige:t01_ejes"
                    serverType="geoserver"
                    :transition="0"
                    :params="{}"/>
            </ol-tile-layer>
            <ol-image-layer :zIndex="1000" :visible=true>
                <ol-source-image-wms
                    url="http://localhost:8080/geoserver/wms"
                    layers="tsige:t00departamento"
                    serverType="geoserver"
                    :transition="0"
                    :params="{}"/>
            </ol-image-layer>
        </ol-layer-group> -->
        <ol-tile-layer>
            <ol-source-osm />
        </ol-tile-layer>

        <!-- Punto de ubicacion de usuario -->
        <ol-geolocation :projection="projectionName" @change:position="geolocationChanged">
            <template>
                <ol-vector-layer :zIndex="1002">
                    <ol-source-vector>
                        <ol-feature ref="positionFeature">
                            <ol-geom-point :coordinates="store.currentGeolocation"></ol-geom-point>
                            <ol-style>
                                <ol-style-circle radius="6">
                                    <ol-style-fill color="#FF3A69"></ol-style-fill>
                                    <ol-style-stroke
                                        color="white"
                                        width="2"
                                    ></ol-style-stroke>
                                </ol-style-circle>
                            </ol-style>
                        </ol-feature>
                    </ol-source-vector>
                </ol-vector-layer>
            </template>
        </ol-geolocation>
        
        <!--Punto de solicitud -->
        <ol-vector-layer :zIndex="1009">
            <ol-source-vector>
                <ol-feature ref="posicionSolicitud">
                    <ol-geom-point :coordinates="store.puntoSolicitud"></ol-geom-point>
                    <ol-style>
                        <ol-style-icon src="PuntoSolicitud.png" :anchor=anchor anchorXUnits="fraction" anchorYUnits="pixels" :scale="0.28"></ol-style-icon>      
                    </ol-style>
                </ol-feature>
            </ol-source-vector>
        </ol-vector-layer>

        <!-- Interaccion para obtener coordenadas de cursor -->
        <!-- <ol-interaction-draw
          ref="indicadorClick"
          :type="store.tipoInteraccion"
          @drawstart="drawstart"
          @drawend="drawend"
        >
        </ol-interaction-draw> -->

        
    </ol-map>

    

  </template>
  

  
  <style >
  .ol-map {
    position: relative;
  }
  .ol-map-loading:after {
    content: "";
    box-sizing: border-box;
    position: absolute;
    top: 50%;
    left: 50%;
    width: 80px;
    height: 80px;
    margin-top: -40px;
    margin-left: -40px;
    border-radius: 50%;
    border: 5px solid rgba(255, 255, 255, 0);
    border-top-color: #FF3A69;
    animation: spinner 0.6s linear infinite;
  }
  
  @keyframes spinner {
    to {
      transform: rotate(360deg);
    }
  }

  .ol-zoom {
     left: 8px;
     right: auto;
     top: auto !important;
     bottom: 8px;
  }

  .ol-zoom-in {
     background-color: v-bind(obtenerColorUsuario)!important;
     color: white !important;
  }

  .ol-zoom-out {
     background-color: v-bind(obtenerColorUsuario)!important;
     color: white !important;
  }

  .overlay{
    position: absolute;
    z-index:10000;
    display: flex;
    width: 100%;
    justify-content: space-between;
  }

  .boton-poligono{
    margin: 10px;
  }

  .boton-ubicacion{
    margin-left: 10px;
    margin-top: 3px;
  }

  .barra-y-boton{
    display: flex;
    margin-top:  10px;
    margin-left: 10px;

  }

  .barra-solicitud{
    height: 20px;
    width: 70px;
    border-radius: 50%;
    background-color: white;
  }


  </style>