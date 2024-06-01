<script>
import { store } from '../../store.js'
import axios from 'axios';
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
    <div style="padding: 15px 15px;">
        <p>Esta es la seccion de datos de desarrollo. No va a estar en el resultado final, es solo para
        inspeccionar variables</p>
        <br>
        <p><span class="nombre">geolocation:</span> {{ store.currentGeolocation}}</p>
        <p><span class="nombre">pto solicitud:</span> {{ store.puntoSolicitud}}</p>
        <p><span class="nombre">pto destino:</span> {{ store.puntoDestino}}</p>
        <p><span class="nombre">centro acutal:</span> {{ store.currentCenter}}</p>
        <p><span class="nombre">zoom actual:</span> {{ store.currentZoom}}</p>
        <p><span class="nombre">tipo usuario:</span> {{ store.tipoUsuario}}</p>
        <p><span class="nombre">tipo interaccion:</span> {{ store.tipoInteraccion}}</p>
        <p><span class="nombre">modo interaccion:</span> {{ store.modoInteraccion}}</p>
        <button class="boton" @click="store.usarUbicacion">Ir a ubicacion</button>
        <button class="boton" @click="probarConexion">Probar conexion</button>
        <button class="boton" @click="store.callePtoSolicitud">Calle en punto de solicitud</button>
        <span class="nombre">Tipo interaccion:</span>
        <button class="boton" @click="store.agregarInteraccion('Point')">Cambiar a punto</button>
        <button class="boton" @click="store.agregarInteraccion('LineString')">Cambiar a linea</button>
        <span class="nombre">Modo interaccion:</span>
        <button class="boton" @click="store.modoInteraccion = undefined">Cambiar a modo normal</button>
        <button class="boton" @click="store.modoInteraccion = 'punto-solicitud'">Cambiar a modo pto solicitud</button>
        <button class="boton" @click="store.modoInteraccion = 'punto-sucursal'">Cambiar a modo pto sucursal</button>
        <button class="boton" @click="store.modoInteraccion = 'recorrido-auto'">Cambiar a modo recorrido auto</button>
    </div>
</template>

<style scoped>
.nombre{
    font-weight: 600;
    text-decoration: underline;
}
</style>