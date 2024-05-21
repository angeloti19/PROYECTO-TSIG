<script>
import axios from 'axios';
import { store } from '@/store';
import ContenedorCerrable from '../ContenedorCerrable.vue'

export default {
    name: 'nuevaSucursalModal',
    components: {
        ContenedorCerrable
    },
    data() {
        return {
            store,
            visible: false,
            sucursalCreada: false,
            nombre: "",
            x: 0,
            y: 0,
            isLoading: false
        }
    },
    emits: ['refetch'],
    methods: {
        cambiarVisibilidad(valor) {
            if (valor) {
                //Cuando se abre el modal
                this.sucursalCreada = false
            }
            this.visible = valor
        },
        abrir(){
            this.resetDatos()
            this.cambiarVisibilidad(true)
        },
        resetDatos(){
            this.nombre = ""
            this.x = 0
            this.y = 0
        },
        async onSubmitCrearSucursal(){
            if(this.nombre == "" || this.x == "" || this.y == ""){
                alert("datos incorrectos")
                return
            }
            this.isLoading = true
            //Hace el pedido
            const data = {
                "nombre": this.nombre,
                "coordenadas": {
                    "x": this.x,
                    "y": this.y
                }
            }
            const response = await axios.post(import.meta.env.VITE_BACKEND_API + "api/automotora/" + this.automotoraId + "/sucursal", data)
                .then(async function (response) {
                    this.sucursalCreada = true
                    this.isLoading = false
                    this.store.fetchSucursalesMapa()
                    this.$emit('refetch')
                }.bind(this))
                .catch(function (error) {
                    console.log("Error: " + error);
                    this.isLoading = false
                }.bind(this));

        },
        marcarUbicacion(){
            //Esta funcion inicia el modo de interaccion para colocar el punto en el mapa
            store.funcionUbicacionSucursal = this.setUbicacion
            store.modoInteraccion = "punto-sucursal"
            this.cambiarVisibilidad(false)
        },
        setUbicacion(coordenada){
            //Esta funcion es llamada cuando se coloca el punto en el mapa
            console.log("RECIVI COORDENADA: " + coordenada)
            store.modoInteraccion = "punto-solicitud"
            this.x = coordenada[0]
            this.y = coordenada[1]
            this.cambiarVisibilidad(true)
        }
    },
    props: {
        automotoraId : Number
    },
    mounted() {
    }
}
</script>

<template>
    <v-dialog  opacity="0.05" v-model="visible" width="auto" persistent scrollable>
        <template v-if="!sucursalCreada"> <!-- Sucursal por agregar -->
            <ContenedorCerrable style="width: 462px;" @cerrar="cambiarVisibilidad(false)">
                <template v-slot:titulo>
                    Nueva sucursal
                </template>
                <template v-slot:contenido>
                    <form> <!-- Formulario -->
                        <div class="contenedor-formulario">
                            <label for="nombre">Nombre</label>
                            <input v-model="nombre" name="nombre" id="nombre" type="text">
                            <label>Ubicación</label>
                            <div class="contenedor-form-ubicacion">
                                <div style="display:flex;">
                                    <div>
                                        <label style="font-size: 14px;" for="longitud">Longitud</label>
                                        <input style="width:176px" v-model="x" name="longitud" id="longitud" type="text">
                                    </div>
                                    <div>
                                        <label style="font-size: 14px;" for="latitud">Latitud</label>
                                        <input style="width:177px" v-model="y" name="latitud" id="latitud" type="text">
                                    </div>
                                </div>
                                <button style="width: 253px" type="button" @click="marcarUbicacion" class="boton con-borde">Marcar ubicación en mapa</button>
                            </div>
                            <button :disabled="isLoading" type="button" @click="onSubmitCrearSucursal" class="boton con-borde primario" style="margin-top: 15px; width: 140px">Confirmar</button>
                        </div>
                    </form>
                </template>
            </ContenedorCerrable>
        </template>
        <template v-else> <!-- Sucursal agregada -->
            <ContenedorCerrable style="padding-bottom: 34px;" @cerrar="cambiarVisibilidad(false)">
                <template v-slot:titulo>
                    Sucursal agregada
                </template>
                <template v-slot:contenido>
                    <div style="margin-top:15px">
                        <p>Se agregó la sucursal exitosamente.</p>
                    </div>
                    <button style="display: block; margin-top: 25px;" @click="cambiarVisibilidad(false)">
                        Cerrar</button>
                </template>
            </ContenedorCerrable>
        </template>
    </v-dialog>
</template>

<style scoped>

.contenedor-form-ubicacion{
    margin-left: 15px; 
    margin-top: 10px; 
    padding-left: 10px; 
    border-left-style: solid; 
    border-left-color: rgba(0, 0, 0, 0.329); 
    border-left-width: 2px;

}


</style>
