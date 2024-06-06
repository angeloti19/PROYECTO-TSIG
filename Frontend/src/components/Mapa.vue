<script>
import { store } from '@/store'
import BotonCircular from './BotonCircular.vue';
import logo from './icons/logo.vue';
import poligono from './icons/poligono.vue'
import ubicacion from './icons/ubicacion.vue'
import locationTarget from './icons/locationTarget.vue'
import Map from "ol/Map";
import BusquedaEspecifica from './BusquedaEspecifica.vue';

export default {
    name: 'mapa',
    components: {
        BotonCircular,
        poligono,
        ubicacion,
        BusquedaEspecifica,
        locationTarget
    },
    data() {
        return {
            store,
            projectionName: 'EPSG:32721',
            projectionDef: '+proj=utm +zone=21 +south +datum=WGS84 +units=m +no_defs +type=crs',
            projectionExtent: [166021.44, 1116915.04, 833978.56, 10000000.0],
            anchor: [0.517, 150],
            montevideoExtent: [550943, 6132427, 591081, 6161393]
        }
    },
    methods: {
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
        geolocationChanged(event) {
            this.store.currentGeolocation = event.target.getPosition();
            //this.$refs.view.setCenter(event.target.getPosition());
            //this.puntoSolicitud = event.target.getPosition();
        }
    },
    mounted() {
        store.viewReference = this.$refs.view
        store.mapReference = this.$refs.mapref.map
        store.agregarInteraccion("Point")
        if (store.tipoUsuario == "admin") {
            store.modoInteraccion = undefined
            store.fetchSucursalesMapa("") //Hacer un fetch con filtro "" hace que sobreescriba el filtro anterior
            store.fetchAutosMapa("")
        } else {
            store.modoInteraccion = "punto-solicitud"
            store.autosSucursalCercanos()
        }


    },
    computed: {
        obtenerColorUsuario() {
            if (this.store.tipoUsuario == "anonimo") {
                return "#26A099"
            } else {
                return "#FF3A69"
            }
        }
    }
}

</script>

<template>
    <ol-map ref="mapref" :loadTilesWhileAnimating="true" :loadTilesWhileInteracting="true" style="height: 100%">
        <ol-projection-register :projectionName="projectionName" :projectionDef="projectionDef"
            :projectionExtent="projectionExtent" />

        <!-- Div overlay -->
        <div class="overlay">
            <div class="barra-y-boton">
                <div v-show="store.modoInteraccion == 'punto-solicitud' || store.modoInteraccion == undefined || store.modoInteraccion == 'punto-destino'"
                    style="height: 20px;">
                    <v-expansion-panels>
                        <v-expansion-panel
                            :title="store.tipoUsuario == 'anonimo' ? 'Cambiar punto de solicitud' : 'Búsqueda rápida'"
                            style="width: 275px;">
                            <v-expansion-panel-text>
                                <BusquedaEspecifica />
                            </v-expansion-panel-text>
                        </v-expansion-panel>
                    </v-expansion-panels>
                </div>

                <BotonCircular
                    v-show="store.tipoUsuario == 'anonimo' && (store.modoInteraccion == 'punto-solicitud' || store.modoInteraccion == undefined || store.modoInteraccion == 'punto-destino')"
                    @click="store.usarUbicacion()" class="boton-ubicacion" :color="obtenerColorUsuario">
                    <locationTarget />
                </BotonCircular>

                <v-btn-toggle v-if="store.tipoUsuario == 'anonimo'" rounded="xl" density="comfortable"
                    v-model="store.modoInteraccion"
                    style="margin-top: 3px; margin-left: 10px; border-color: rgba(0, 0, 0, 0.387); border-width: 2px; border-style: solid;">
                    <v-btn disabled icon="mdi-map-marker" style="border-right-style: solid;border-right-width: 2px;border-right-color: #858585;"></v-btn>
                    <v-btn autofocus class="btn-sol" value="punto-solicitud"
                        style="text-transform: none; padding: 5px; border-right: 2px solid rgba(0, 0, 0, 0.387);">Solicitud</v-btn>
                    <v-btn class="btn-des" value="punto-destino"
                        style="text-transform: none; padding: 5px; padding-right: 10px;">Destino</v-btn>
                </v-btn-toggle>
            </div>

            <BotonCircular v-show="store.tipoUsuario == 'anonimo'" @click="store.iniciarBusquedaPoligono()"
                class="boton-poligono" :color="obtenerColorUsuario">
                <poligono />
            </BotonCircular>
        </div>

        <ol-view ref="view" :center="store.center" :rotation="store.rotation" :zoom="store.zoom"
            :projection="projectionName" :extent="montevideoExtent" @change:center="centerChanged"
            @change:resolution="resolutionChanged" @change:rotation="rotationChanged" />
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
        <ol-geolocation :projection="projectionName" @change:position="geolocationChanged"
            v-if="store.modoInteraccion == 'punto-solicitud'">
            <template>
                <ol-vector-layer :zIndex="1002">
                    <ol-source-vector>
                        <ol-feature ref="positionFeature">
                            <ol-geom-point :coordinates="store.currentGeolocation"></ol-geom-point>
                            <ol-style>
                                <ol-style-circle radius="6">
                                    <ol-style-fill color="#FF3A69"></ol-style-fill>
                                    <ol-style-stroke color="white" width="2"></ol-style-stroke>
                                </ol-style-circle>
                            </ol-style>
                        </ol-feature>
                    </ol-source-vector>
                </ol-vector-layer>
            </template>
        </ol-geolocation>

        <!--Punto de solicitud -->
        <ol-vector-layer :zIndex="1009"
            v-if="store.puntoSolicitud != undefined && store.tipoUsuario == 'anonimo'">
            <ol-source-vector>
                <ol-feature ref="posicionSolicitud">
                    <ol-geom-point :coordinates="store.puntoSolicitud"></ol-geom-point>
                    <ol-style>
                        <ol-style-icon src="PuntoSolicitud.png" :anchor=anchor anchorXUnits="fraction"
                            anchorYUnits="pixels" :scale="0.28"></ol-style-icon>
                    </ol-style>
                </ol-feature>
            </ol-source-vector>
        </ol-vector-layer>

        <!--Punto de destino -->
        <ol-vector-layer :zIndex="1009"
            v-if="store.puntoDestino != undefined && store.tipoUsuario == 'anonimo'">
            <ol-source-vector>
                <ol-feature ref="posicionDestino">
                    <ol-geom-point :coordinates="store.puntoDestino"></ol-geom-point>
                    <ol-style>
                        <ol-style-icon src="PuntoDestino.png" :anchor=anchor anchorXUnits="fraction"
                            anchorYUnits="pixels" :scale="0.28"></ol-style-icon>
                    </ol-style>
                </ol-feature>
            </ol-source-vector>
        </ol-vector-layer>
    </ol-map>

</template>



<style>
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
    pointer-events: none;
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
    background-color: v-bind(obtenerColorUsuario) !important;
    color: white !important;
}

.ol-zoom-out {
    background-color: v-bind(obtenerColorUsuario) !important;
    color: white !important;
}

.overlay {
    position: absolute;
<<<<<<< HEAD
    z-index:1;
=======
    z-index: 10000;
>>>>>>> 207eb9927c268ab959a79a2517a1e8bdc4e13def
    display: flex;
    width: 100%;
    justify-content: space-between;
}

.boton-poligono {
    margin: 10px;
}

.boton-ubicacion {
    margin-left: 10px;
    margin-top: 3px;
}

.barra-y-boton {
    display: flex;
    margin-top: 10px;
    margin-left: 10px;

}

.barra-solicitud {
    height: 20px;
    width: 70px;
    border-radius: 50%;
    background-color: white;
}

.btn-des {
    color: #26a09a !important;
    background-color:#dffafa !important;
}

.btn-sol {
    color: #ff3a68 !important;
    background-color: #ffe4e1 !important;
}

.btn-des:hover,
.btn-des:active {
    background-color: #26A099 !important;
    color: white !important;
}

.btn-sol:hover,
.btn-sol:active {
    background-color: #FF3A69 !important;
    color: white !important;
}

.btn-sol.v-btn--active{
    background-color: #FF3A69 !important;
    color: white !important;
}

.btn-des.v-btn--active{
    background-color: #26A099 !important;
    color: white !important;
}
</style>