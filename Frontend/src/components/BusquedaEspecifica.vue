<template>
    <input type="text" placeholder="Origen" id="origen"/>
    <input type="text" placeholder="Destino" id="destino" v-if="store.tipoUsuario === 'anonimo'"/>     
</template>

<script>
import { store } from '@/store';
import proj4 from 'proj4';

proj4.defs("EPSG:32721","+proj=utm +zone=21 +south +datum=WGS84 +units=m +no_defs +type=crs");

export default{

    data() {
        return{
            store
        }
    },

    mounted(){
        const origenAutocomplete = new google.maps.places.Autocomplete(
            document.getElementById("origen"), {
            bounds: new google.maps.LatLngBounds(
                new google.maps.LatLng(-34.8836, -56.1819)
            ),
            componentRestrictions: { country: 'UY' }
        }
        );
        
        origenAutocomplete.addListener("place_changed", ()=>{
            let [x, y] = proj4("EPSG:4326", "EPSG:32721", [origenAutocomplete.getPlace().geometry.location.lng(), origenAutocomplete.getPlace().geometry.location.lat()]);
            this.store.puntoSolicitud = [x, y];
            this.store.centrarMapaEnCoordenada([x,y]);
        })
        if (this.store.tipoUsuario === 'anonimo'){
            const destinoAutocomplete = new google.maps.places.Autocomplete(
                document.getElementById("destino"), {
                    bounds: new google.maps.LatLngBounds(
                        new google.maps.LatLng(-34.8836, -56.1819)
                    ),
                    componentRestrictions: { country: 'UY' }
                }
            )

            destinoAutocomplete.addListener("place_changed", ()=>{
                let [x, y] = proj4("EPSG:4326", "EPSG:32721", [destinoAutocomplete.getPlace().geometry.location.lng(), destinoAutocomplete.getPlace().geometry.location.lat()]);
                this.store.puntoDestino = [x, y];
                this.store.centrarMapaEnCoordenada([x,y]);
        })
        }
        

    }
};


</script>