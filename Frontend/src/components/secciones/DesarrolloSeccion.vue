<script>
import { store } from '../../store.js'
export default{
    name: 'desarrolloSeccion',
    components: {

    },
    data(){
        return{
            store
        }
    },
    mounted(){
    },
    props:{
    },
    methods:{
    },
}
</script>

<template>
    <div>
        <p>Esta es la seccion de datos de desarrollo. No va a estar en el resultado final, es solo para
        inspeccionar variables</p>
        <br>
        <p><span class="nombre">geolocation:</span> {{ store.currentGeolocation}}</p>
        <p><span class="nombre">pto solicitud:</span> {{ store.puntoSolicitud}}</p>
        <p><span class="nombre">centro acutal:</span> {{ store.currentCenter}}</p>
        <p><span class="nombre">zoom actual:</span> {{ store.currentZoom}}</p>
        <p><span class="nombre">tipo usuario:</span> {{ store.tipoUsuario}}</p>
        <button class="boton" @click="store.usarUbicacion">Ir a ubicacion</button>

    </div>
</template>

<style scoped>
.nombre{
    font-weight: 600;
    text-decoration: underline;
}
</style>