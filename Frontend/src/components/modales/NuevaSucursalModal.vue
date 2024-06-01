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
            isLoading: false,
            id: undefined,
            mensajeError: ""
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
            if(!this.editar){
                this.resetDatos()
            }
            this.cambiarVisibilidad(true)
        },
        resetDatos(){
            this.nombre = ""
            this.x = 0
            this.y = 0
            this.mensajeError = ""
        },
        setDatosEditar(id, nombre, x, y){
            this.nombre = nombre
            this.x = x
            this.y = y
            this.id = id
            this.mensajeError = ""
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
                    this.mensajeError = error.response.data
                    this.isLoading = false
                }.bind(this));

        },
        async onSubmitEditarSucursal(){
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
            const response = await axios.put(import.meta.env.VITE_BACKEND_API + "api/automotora/" + this.automotoraId + "/sucursal/" + this.id, data)
                .then(async function (response) {
                    this.sucursalCreada = true
                    this.isLoading = false
                    this.store.fetchSucursalesMapa()
                    this.$emit('refetch')
                }.bind(this))
                .catch(function (error) {
                    this.mensajeError = error.response.data
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
            store.modoInteraccion = undefined
            this.x = coordenada[0]
            this.y = coordenada[1]
            this.cambiarVisibilidad(true)
        }
    },
    props: {
        automotoraId : Number,
        editar : Boolean
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
                    {{!editar ? 'Nueva sucursal' : 'Editar sucursal'}}
                </template>
                <template v-slot:contenido>
                    <span style="color: red; font-size: 10px;">{{ mensajeError }}</span>
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
                            <button :disabled="isLoading" type="button" @click="!editar ? onSubmitCrearSucursal() : onSubmitEditarSucursal()" class="boton con-borde primario" style="margin-top: 15px; width: 140px">Confirmar</button>
                        </div>
                    </form>
                </template>
            </ContenedorCerrable>
        </template>
        <template v-else> <!-- Sucursal agregada -->
            <ContenedorCerrable style="padding-bottom: 34px;" @cerrar="cambiarVisibilidad(false)">
                <template v-slot:titulo>
                    {{!editar ? 'Sucursal agregada' : 'Sucursal editada'}}
                </template>
                <template v-slot:contenido>
                    <div style="margin-top:15px">
                        <p>{{!editar ? 'Se agregó la sucursal exitosamente.' : 'Se editó la sucursal exitosamente.'}}</p>
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
