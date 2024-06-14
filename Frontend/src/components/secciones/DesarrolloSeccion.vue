<script>
import { reset } from 'ol/transform.js';
import { store } from '../../store.js'
import axios from 'axios';
export default {
    name: 'desarrolloSeccion',
    components: {

    },
    data() {
        return {
            store
        }
    },
    computed: {
        userType() {
            if (this.store.tipoUsuario == "anonimo") {
                return "guest-class"
            } else {
                return "admin-class"
            }
        }
    },
    mounted() {
    },
    props: {
    },
    methods: {
        async probarConexion() {
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
        },
        resetLocalStorage() {
            localStorage.clear();
        }
    },
}
</script>

<template>
    <div style="padding: 15px 15px;">
        <p>Esta es la seccion de datos de desarrollo. No va a estar en el resultado final, es solo para
            inspeccionar variables</p>
        <br>
        <button type="button" @click="resetLocalStorage" :class="['boton', userType]">Reset Local Storage</button>
        <p><span class="nombre">geolocation:</span> {{ store.currentGeolocation }}</p>
        <p><span class="nombre">pto solicitud:</span> {{ store.puntoSolicitud }}</p>
        <p><span class="nombre">pto destino:</span> {{ store.puntoDestino }}</p>
        <p><span class="nombre">centro acutal:</span> {{ store.currentCenter }}</p>
        <p><span class="nombre">zoom actual:</span> {{ store.currentZoom }}</p>
        <p><span class="nombre">tipo usuario:</span> {{ store.tipoUsuario }}</p>
        <p><span class="nombre">tipo interaccion:</span> {{ store.tipoInteraccion }}</p>
        <p><span class="nombre">modo interaccion:</span> {{ store.modoInteraccion }}</p>
        <button :class="['boton', userType]" @click="store.usarUbicacion">Ir a ubicacion</button>
        <button :class="['boton', userType]" @click="probarConexion">Probar conexion</button>
        <button :class="['boton', userType]" @click="store.callePtoSolicitud">Calle en punto de
            solicitud</button>
        <span class="nombre">Tipo interaccion:</span>
        <button :class="['boton', userType]" @click="store.agregarInteraccion('Point')">Cambiar a
            punto</button>
        <button :class="['boton', userType]" @click="store.agregarInteraccion('LineString')">Cambiar a
            linea</button>
        <span class="nombre">Modo interaccion:</span>
        <button :class="['boton', userType]" @click="store.modoInteraccion = undefined">Cambiar a modo
            normal</button>
        <button :class="['boton', userType]" @click="store.modoInteraccion = 'punto-solicitud'">Cambiar a modo
            pto
            solicitud</button>
        <button :class="['boton', userType]" @click="store.modoInteraccion = 'punto-sucursal'">Cambiar a modo
            pto
            sucursal</button>
        <button :class="['boton', userType]" @click="store.modoInteraccion = 'recorrido-auto'">Cambiar a modo
            recorrido
            auto</button>
        <button :class="['boton', userType]" @click="store.eliminarInteraccion()">Eliminar
            interaccion</button>
    </div>
</template>

<style scoped>
.nombre {
    font-weight: 600;
    text-decoration: underline;
}

.guest-class {
    background-color: #36454F;
    border-color: white;
    color: white;
}

.guest-class:hover {
    background-image: linear-gradient(#36454F, #4d6270);
}

.admin-class:hover {
    background-image: linear-gradient(#ffffff, lightgray);
}

.admin-class {
    border: 2px solid gray !important;
    background-color: #ffffff;
    color: rgba(0, 0, 0, 0.82);
}

.boton {
    display: block;
    width: 100%;
    padding: 10px 15px;
    cursor: pointer;
    margin-bottom: 10px;
}
</style>