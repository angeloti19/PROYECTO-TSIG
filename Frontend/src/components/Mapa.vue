<script>
import { store } from '@/store'
import BotonCircular from './BotonCircular.vue';
import poligono from './icons/poligono.vue'
import ubicacion from './icons/ubicacion.vue'
import locationTarget from './icons/locationTarget.vue'
import BusquedaEspecifica from './BusquedaEspecifica.vue';
import AlertModal from '@/components/modales/AlertModal.vue';
import axios from 'axios';

export default {
    name: 'mapa',
    components: {
        BotonCircular,
        poligono,
        ubicacion,
        BusquedaEspecifica,
        locationTarget,
        AlertModal
    },
    data() {
        return {
            store,
            projectionName: 'EPSG:32721',
            projectionDef: '+proj=utm +zone=21 +south +datum=WGS84 +units=m +no_defs +type=crs',
            projectionExtent: [166021.44, 1116915.04, 833978.56, 10000000.0],
            anchor: [0.517, 150],
            montevideoExtent: [550943, 6132427, 591081, 6161393],
            selectedFeature: null // Variable para almacenar el feature seleccionado
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
            this.store.usarUbicacion()
            //this.$refs.view.setCenter(event.target.getPosition());
            //this.puntoSolicitud = event.target.getPosition();
        },
        async handleMapClick (event) {
            if(store.modoInteraccion != undefined){
                return 
            }

            this.$refs.alertModal.setContenido("Propiedades auto: ", "cargando");
            this.$refs.alertModal.abrir();

            const x = Math.floor(event.pixel[0]);
            const y = Math.floor(event.pixel[1]);

            const urlAuto = 'http://localhost:8080/geoserver/wms' +
                '?SERVICE=WMS&' +
                'VERSION=1.1.0&' +
                'REQUEST=GetFeatureInfo&' +
                'TYPENAME=tsige:auto&' +
                'LAYERS=tsige:auto&'+
                'INFO_FORMAT=application/json&' +
                'SRSNAME=EPSG:32721&' +
                'BBOX='+store.mapReference.getView().calculateExtent().join(',') + '&' +
                'WIDTH='+store.mapReference.getSize()[0]+ '&' +
                'HEIGHT='+store.mapReference.getSize()[1]+ '&' +
                'QUERY_LAYERS=tsige:auto&' +
                `X=${x}&` +
                `Y=${y}&`;

                const urlSucursal = 'http://localhost:8080/geoserver/wms' +
                '?SERVICE=WMS&' +
                'VERSION=1.1.0&' +
                'REQUEST=GetFeatureInfo&' +
                'TYPENAME=tsige:sucursal&' +
                'LAYERS=tsige:sucursal&'+
                'INFO_FORMAT=application/json&' +
                'SRSNAME=EPSG:32721&' +
                'BBOX='+store.mapReference.getView().calculateExtent().join(',') + '&' +
                'WIDTH='+store.mapReference.getSize()[0]+ '&' +
                'HEIGHT='+store.mapReference.getSize()[1]+ '&' +
                'QUERY_LAYERS=tsige:sucursal&' +
                `X=${x}&` +
                `Y=${y}&`;

            console.log("URL AUTO: "+ urlAuto);
            console.log("URL SUCURSAL: "+ urlSucursal);

            Promise.all([fetch(urlAuto), fetch(urlSucursal)])
                .then(responses => Promise.all(responses.map(response => {
                if (!response.ok) {
                    return response.text().then(text => { throw new Error(text) });
                }
                    return response.json();
                })))
                .then(async dataArray => {
                    const [dataAuto, dataSucursal] = dataArray;

                    console.log("DataAuto: ", dataAuto);
                    console.log("DataSucursal: ", dataSucursal);

                    // Extrae las características directamente
                    const featuresAuto = dataAuto.features;
                    const featuresSucursal = dataSucursal.features;

                    console.log("featuresAuto: ", featuresAuto);
                    console.log("featuresSucursal: ", featuresSucursal);

                    if (featuresAuto && featuresAuto.length > 0) {
                        this.selectedFeature = featuresAuto[0].automotora_id; // Aquí guardamos el primer feature de autos
                        console.log("selectedFeature: ", this.selectedFeature);
                        this.$refs.alertModal.setContenido("Propiedades auto: ", this.selectedFeature);
                        
                    } else if(featuresSucursal && featuresSucursal.length > 0) {

                        this.selectedFeature = featuresSucursal[0];  // Aquí guardamos el primer feature de autos

                        const response = await axios.get('/api/automotora/'+featuresSucursal[0].properties.automotora_id)
                        .then(function (response) {
                            this.$refs.alertModal.setContenido("Propiedades sucursal: ", response.data );
                        }.bind(this))
                        .catch(function (error) {
                            console.log("Error: " + error.response.data);
                        }.bind(this));
                    }else{
                        this.$refs.alertModal.setContenido("Error: ", "No se encontro informacion en el punto");
                        this.selectedFeature = null;
                    }
                })
                .catch(error => {
                    console.error("Error fetching WFS GetFeatureInfo:", error);
                });
    },
    closeModal() {
      this.selectedFeature = null;
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
            store.modoInteraccion = undefined
            
            store.autosSucursalCercanos()
        }

        store.mapReference.on('singleclick', this.handleMapClick);

    },
    computed: {
        obtenerColorUsuario() {
            if (this.store.tipoUsuario == "anonimo") {
                return "#26A099"
            } else {
                return "#36454F"
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
            <div v-if="store.mapaCompleto" class="ayuda-interaccion">
                <span>ⓘ</span>
                <div style="" v-if="store.modoInteraccion == 'punto-sucursal'">
                    <p>Seleccione la ubicación de la sucursal.</p>
                </div>
                <div v-if="store.modoInteraccion == 'recorrido-auto'">
                    <p>Seleccione el recorrido del auto.</p>
                    <p>Para terminar el recorrido, haga click en el último punto.</p>
                </div>
                <div v-if="store.modoInteraccion == 'poligono-autos'">
                    <p>Dibuje una zona para ver los autos que se encuentran en ella.</p>
                    <p>Para completar el polígono, haga click en el primer punto.</p>
                </div>
            </div>
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

                <v-btn-toggle v-if="store.tipoUsuario == 'anonimo' && !store.mapaCompleto" rounded="xl" density="comfortable"
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
            v-if="store.tipoUsuario == 'anonimo'">
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

        <AlertModal ref="alertModal"/>

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
    z-index:1;
    display: flex;
    width: 100%;
    justify-content: space-between;
    pointer-events: none;
}

.overlay > *{
    pointer-events: all;
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

.ayuda-interaccion{
    background-color: white;
    color: #000000c4;
    padding: 10px 15px;
    border-radius: 20px;
    border-style: solid;
    border-width: 2px;
    border-color: #a2a2a2;
    font-size: 13px;
    display: flex;
    gap: 10px;
    pointer-events: none;
    opacity: 0.77;
    margin: 10px;
}
</style>