<script>
import { store } from '../../store.js'
import axios from 'axios';
export default{
    name: 'desarrolloSeccion',
    components: {

    },
    data(){
        return{
            store,
            calleFiltro: ""
        }
    },
    mounted(){
        console.log(import.meta.env.VITE_BACKEND_API);
    },
    props:{
    },
    methods:{
        async probarConexion(){
            const data = {
                "nombre": "abcd",
                "correo": "abcd@gmail.com",
                "contrasenia": "abcd123",
            }
            const response = await axios.post(import.meta.env.VITE_BACKEND_API + "api/usuario", data)
                .then(function (response) {
                    console.log("Response: " + response);

                }.bind(this))
                .catch(function (error) {
                    console.log("Error: " + error);
                }.bind(this));
        }
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
        <button class="boton" @click="probarConexion">Probar conexion</button>
        <button class="boton" @click="store.callePtoSolicitud">Calle en punto de solicitud</button>
        <p><span class="nombre">Agregar calle por filtro</span></p>
        <input v-model="calleFiltro" type="text" placeholder="Escriba el nombre de la calle">
        <!-- Botón para agregar la capa con filtro -->
        <button @click="store.agregarCapaConFiltro(calleFiltro)">Agregar Capa con Filtro</button>

    </div>
</template>

<style scoped>
.nombre{
    font-weight: 600;
    text-decoration: underline;
}
</style>